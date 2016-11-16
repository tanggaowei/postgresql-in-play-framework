使用 PostgreSQL 可以快速查询某一点到数据表存在的其它点之间的距离，并且能按距离远近进行顺序。
 
当我们要查询附近的人或物时，这个非常实用！

 **1、环境** 


- Play Framework 1.4.3
- PostgreSQL 9.6.2
- PostGIS 2.3
- Java 7

 **2、依赖** 



- postgresql-9.4.1212.jre7.jar
- hibernate-spatial-4.0.1.jar
- jts-1.13.jar
- postgis-jdbc-2.1.7.jar
 
这 4 个 jar 包可以从百度云下载：http://pan.baidu.com/s/1dEU7x9B

 **3、application.conf 数据库连接配置** 

```
db.default.url=jdbc:postgresql:test
db.default.driver=org.postgresql.Driver
db.default.user=postgres
db.default.pass=tgw@123
jpa.default.dialect=org.hibernate.spatial.dialect.postgis.PostgisDialect
```

4、给需要使用的数据库添加 PostGIS 扩展

在对应的数据库上执行下列查询语句：

```
-- Enable PostGIS (includes raster)
CREATE EXTENSION postgis;
-- Enable Topology
CREATE EXTENSION postgis_topology;
-- Enable PostGIS Advanced 3D 
-- and other geoprocessing algorithms
-- sfcgal not available with all distributions
CREATE EXTENSION postgis_sfcgal;
-- fuzzy matching needed for Tiger
CREATE EXTENSION fuzzystrmatch;
-- rule based standardizer
CREATE EXTENSION address_standardizer;
-- example rule data set
CREATE EXTENSION address_standardizer_data_us;  
-- Enable US Tiger Geocoder
CREATE EXTENSION postgis_tiger_geocoder;
```

> 提示：如果不给数据库添加 PostGIS 扩展，Model 里面的 geometry(Point,4326) 配置将会报错！

5、使用

BaseModel.java - 配置 PostgreSQL 数据库的自增 ID：

```
package models;
 
import play.db.jpa.GenericModel; 
import javax.persistence.*;
 
/**
* Created by TangGaowei on 2016/11/15.
*/
@MappedSuperclass
public class BaseModel extends GenericModel {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
 
    public Long getId() {
        return id;
    }
 
    @Override
    public Object _key() {
        return getId();
    }
}
```

UserModel.java - Model 中 PostGIS 地理字段的定义方法：

```
package models;
 
import javax.persistence.*;
import org.hibernate.annotations.Type;
import com.vividsolutions.jts.geom.Point;
 
@Entity
public class UserModel extends BaseModel {
    public String name; // 姓名
 
    @Column(columnDefinition = "geometry(Point,4326)")
    @Type(type = "org.hibernate.spatial.GeometryType")
    public Point location;; // 所在位置
 
    public Integer state; // 状态
}
```

Application.java - Model 的新建与修改：

```
package controllers;
 
import com.vividsolutions.jts.geom.*;
import org.hibernate.spatial.jts.mgeom.MCoordinateSequence;
import org.hibernate.spatial.jts.mgeom.MGeometryFactory;
import play.mvc.*;
import models.*;
 
public class Application extends Controller {
 
    public static void index() {
        UserModel user = UserModel.find("name=?", "test").first();
        if(user == null){
            user = new UserModel();
            user.name = "test";
            user.save();
        }
        else{
            // 修改地理位置字段
            MGeometryFactory mgf = new MGeometryFactory(new PrecisionModel(), 4326);
            Coordinate coord = new Coordinate( 32.581112, 124.381802 );
            MCoordinateSequence coordinates = new MCoordinateSequence(new Coordinate[]{coord});
            Point location =  mgf.createPoint(coordinates);
            user.location = location;
            user.save();
        }
        renderText("hello");
    }
 
}
```

