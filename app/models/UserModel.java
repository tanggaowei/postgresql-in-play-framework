package models;


import com.vividsolutions.jts.geom.Point;
import org.hibernate.annotations.Type;
import javax.persistence.*;

/**
 * INSERT INTO "public"."t_user"(name,location) VALUES('黄鹤楼',ST_GeomFromText('POINT(30.544463 114.302301)',4326));
 * INSERT INTO "public"."t_user"(name,location) VALUES('东湖磨山景区',ST_GeomFromText('POINT(30.553443 114.414567)',4326));
 * INSERT INTO "public"."t_user"(name,location) VALUES('古琴台',ST_GeomFromText('POINT(30.553748 114.262789)',4326));
 * UPDATE "public"."t_user" SET location = ST_GeomFromText('POINT(31.096249 114.472448)',4326) WHERE name='test';
 * SELECT name, ST_AsText(location), ST_AsEwkt(location), ST_X(location), ST_Y("location") FROM t_user;
 * SELECT name, ST_X(location), ST_Y(location), ST_Distance_Sphere(location,ST_GeomFromText('POINT(30.553748 114.262789)',4326)) dist FROM t_user ORDER BY dist desc
 */
@Entity(name = "t_user")
public class UserModel extends BaseModel {
    public String name; // 姓名

    @Column(columnDefinition = "geometry(Point,4326)")
    @Type(type = "org.hibernate.spatial.GeometryType")
    public Point location;

    public Integer state; // 状态
}
