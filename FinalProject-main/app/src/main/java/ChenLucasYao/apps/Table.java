package ChenLucasYao.apps;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Table extends RealmObject
{
    @PrimaryKey
    private String uuid;

    private String name;

    private String password;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Table{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
