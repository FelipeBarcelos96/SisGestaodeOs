export default class Constants {

    constructor() {
        this.ip = "http://192.168.0.101";
        this.serverPortApi = ":8080";
        this.serverPortRaiz = ":8180";
        this.ipUrl = this.ip + this.serverPortApi;
        this.ipRaiz = this.ip + this.serverPortRaiz;
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

    async join(t, a, s) {
        function format(m) {
            let f = new Intl.DateTimeFormat('en', m);
            return f.format(t);
        }
        return a.map(format).join(s);
    }

    async formatarData(data) {
        let time = new Date(data);
        let dataFormatada = ((time.getDate())) + "/" + ((time.getMonth() + 1)) + "/" + time.getFullYear() +
            " " + time.getHours() + ":" + time.getMinutes() + ":" + time.getSeconds();
        return dataFormatada;
    }

    async formatarDataForm(data) {
        if (null !== data && undefined !== data) {
            let prazo = new Date(data);
            prazo.setMinutes(prazo.getMinutes() - prazo.getTimezoneOffset());
            return prazo.toISOString().slice(0, 16);
        } else {
            return null;
        }
    }

    async formatarDatajson(data) {
        let time = new Date(data);
        let dataFormatada = (time.getFullYear() + "-" + ((time.getMonth() + 1)) + "-" + (time.getDate()) + " " +
            time.getHours() + ":" + time.getMinutes() + ":" + time.getSeconds());
        return dataFormatada;
    }


    /*
    identifyMime(file) {

        let encoded = window.atob(file);
        let extension = undefined;
        let type = undefined;
        var typeAndExtension = null;
        let lowerCase = encoded.toLowerCase();

        var signatures = {
            JVBERi0: "application/pdf",
            R0lGODdh: "image/gif",
            R0lGODlh: "image/gif",
            iVBORw0KGgo: "image/png"
        };

        for (var s in signatures) {
            if (file.indexOf(s) === 0) {
                typeAndExtension = signatures[s];
            }
        }

        if (null !== typeAndExtension) {
            return "data:" + typeAndExtension + ";base64," + file;
        } else {
            if (lowerCase.indexOf("png") !== -1) {
                extension = "png"
                type = "image"
            } else if (lowerCase.indexOf("jpg") !== -1 || lowerCase.indexOf("jpeg") !== -1) {
                extension = "jpg"
                type = "image"
            } else {
                extension = "txt";
                type = "text"
            }
            return "data:" + type + "/" + extension + ";base64," + file;
        }
    };
    */

}