class HttpClientLogin {

    constructor() {

        this.getSinc = function(theUrl) {
            var xmlHttp = new XMLHttpRequest();
            // false para requsição síncrona
            xmlHttp.open("GET", theUrl, false);
            xmlHttp.send(null);
            return xmlHttp.responseText;
        }

        this.getAssinc = function(theUrl, callback) {
            var xmlHttp = new XMLHttpRequest();
            xmlHttp.onreadystatechange = function() {
                if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
                    callback(xmlHttp.responseText);
            }
            xmlHttp.open("GET", theUrl, true); // true for asynchronous 
            xmlHttp.send(null);
        }

        this.delSinc = function(theUrl) {
            var xmlHttp = new XMLHttpRequest();
            xmlHttp.open("DELETE", theUrl, false); // false for synchronous request
            xmlHttp.send(null);
            if (xmlHttp.status === 200)
                alert("Exclusão efetuada com Sucesso");

            if (xmlHttp.status === 400)
                alert(xmlHttp.responseText);

            return xmlHttp.responseText;
        }

        this.putSinc = function(theUrl, json) {
            let jsonString = JSON.stringify(json);
            var xmlHttp = new XMLHttpRequest();
            xmlHttp.open("PUT", theUrl, false); // false for synchronous request
            xmlHttp.setRequestHeader("Content-Type", "application/json");
            xmlHttp.setRequestHeader("Access-Control-Allow-Credentials", "false");
            xmlHttp.setRequestHeader("Access-Control-Allow-Headers", "Date,Transfer-encoding,Content-type");
            xmlHttp.setRequestHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH");
            xmlHttp.setRequestHeader("Access-Control-Allow-Origin", "*");
            xmlHttp.setRequestHeader("Access-Control-Expose-Headers", "Date, Transfer-encoding, Content-type");
            xmlHttp.send(jsonString);

        }

        this.postSincJson = function(theUrl, json) {
            const jsonString = JSON.stringify(json);

            var xmlHttp = new XMLHttpRequest();
            xmlHttp.open("POST", theUrl, false); // false for synchronous request
            xmlHttp.setRequestHeader("Content-Type", "application/json");
            xmlHttp.setRequestHeader("Access-Control-Allow-Credentials", "false");
            xmlHttp.setRequestHeader("Access-Control-Allow-Headers", "Date,Transfer-encoding,Content-type");
            xmlHttp.setRequestHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH");
            xmlHttp.setRequestHeader("Access-Control-Allow-Origin", "*");
            xmlHttp.setRequestHeader("Access-Control-Expose-Headers", "Date, Transfer-encoding, Content-type");

            xmlHttp.send(jsonString);
            return xmlHttp.responseText;
        }

        this.postAssincJson = function(theUrl, jsonString, callback) {
            const json = JSON.stringify(jsonString);

            var xmlHttp = new XMLHttpRequest();

            xmlHttp.open("POST", theUrl, true); // false for synchronous request
            xmlHttp.setRequestHeader("Content-Type", "application/json; charset=utf-8");
            xmlHttp.onreadystatechange = () => {
                if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
                    let obj = JSON.parse(xmlHttp.response);
                    if (obj !== null && obj !== undefined)
                        alert("Cadastrado Realizado com Sucesso!");
                }
            }
            xmlHttp.send(json);
        }

        this.autenticate = function(theUrl, jsonString, callback) {
            var json = JSON.stringify(jsonString);
            var xmlHttp = new XMLHttpRequest();
            //verdadeiro para requisições assícronas.
            xmlHttp.open("POST", theUrl, true);
            xmlHttp.setRequestHeader("Content-Type", "application/json; charset=utf-8");
            xmlHttp.onreadystatechange = () => {
                if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
                    let obj = JSON.parse(xmlHttp.response);
                    if (obj.response) {
                        window.location.replace(obj.url);
                    }
                }
                if (xmlHttp.readyState === 4 && xmlHttp.status === 401) {
                    alert("Login/Senha Inválidos!");
                }
            };
            xmlHttp.send(json);
        }
    }
}