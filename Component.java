package simgui;

public class Component {

    private String name;
    private String id;
    private String type;
    private String connections;

    public Component(String name, String id, String type, String connections) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.connections = connections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConnections() {
        return connections;
    }

    public void setConnections(String connections) {
        this.connections = connections;
    }
}
