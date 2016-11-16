ʹ�� PostgreSQL ���Կ��ٲ�ѯĳһ�㵽���ݱ���ڵ�������֮��ľ��룬�����ܰ�����Զ������˳��
 
������Ҫ��ѯ�������˻���ʱ������ǳ�ʵ�ã�

 **1������** 


- Play Framework 1.4.3
- PostgreSQL 9.6.2
- PostGIS 2.3
- Java 7

 **2������** 



- postgresql-9.4.1212.jre7.jar
- hibernate-spatial-4.0.1.jar
- jts-1.13.jar
- postgis-jdbc-2.1.7.jar
 
�� 4 �� jar �����ԴӰٶ������أ�http://pan.baidu.com/s/1dEU7x9B

 **3��application.conf ���ݿ���������** 

```
db.default.url=jdbc:postgresql:test
db.default.driver=org.postgresql.Driver
db.default.user=postgres
db.default.pass=tgw@123
jpa.default.dialect=org.hibernate.spatial.dialect.postgis.PostgisDialect
```

4������Ҫʹ�õ����ݿ���� PostGIS ��չ

�ڶ�Ӧ�����ݿ���ִ�����в�ѯ��䣺

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

> ��ʾ������������ݿ���� PostGIS ��չ��Model ����� geometry(Point,4326) ���ý��ᱨ��

5��ʹ��

BaseModel.java - ���� PostgreSQL ���ݿ������ ID��

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

UserModel.java - Model �� PostGIS �����ֶεĶ��巽����

```
package models;
 
import javax.persistence.*;
import org.hibernate.annotations.Type;
import com.vividsolutions.jts.geom.Point;
 
@Entity
public class UserModel extends BaseModel {
    public String name; // ����
 
    @Column(columnDefinition = "geometry(Point,4326)")
    @Type(type = "org.hibernate.spatial.GeometryType")
    public Point location;; // ����λ��
 
    public Integer state; // ״̬
}
```

Application.java - Model ���½����޸ģ�

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
            // �޸ĵ���λ���ֶ�
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

