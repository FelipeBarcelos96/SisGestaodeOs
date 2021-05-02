class ConstantsLogin {

    constructor() {
        this.ip = "http://192.168.0.101";
        this.serverPortApi = ":8080";
        this.serverPortRaiz = ":8180";
    }

    async getIp() {
        return this.ip;
    }

    async getServerPort() {
        return this.serverPort;
    }

    getUrl() {
        return this.ip + this.serverPortApi;
    }

    getUrlRaiz() {
        return this.ip + this.serverPortRaiz;
    }
}