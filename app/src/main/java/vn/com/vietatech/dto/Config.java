package vn.com.vietatech.dto;

public class Config {
    private String server;
    private String port;
    private String code; // ma buu cuc

    public String getBda() {
        return bda;
    }

    public void setBda(String bda) {
        this.bda = bda;
    }

    private String bda;

    public Config(String _server, String _port, String _code, String _bda) {
        server = _server;
        port = _port;
        code = _code;
        bda = _bda;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean validServer() {
        return !server.isEmpty();
    }
}
