export default class HttpClient {
    constructor() {
        this.getAssinc = function(theUrl, callback) {
            var xmlHttp = new XMLHttpRequest();
            xmlHttp.onreadystatechange = function() {
                if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
                    callback(xmlHttp.responseText);
            }
            xmlHttp.open("GET", theUrl, true); // true for asynchronous 
            xmlHttp.send(null);
        }

        this.getSinc = function(theUrl) {
            var xmlHttp = new XMLHttpRequest();
            xmlHttp.open("GET", theUrl, false); // false for synchronous request
            xmlHttp.send(null);
            return xmlHttp.responseText;
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
                    console.log(obj);
                }
            }
            xmlHttp.send(json);
        }
    }
}