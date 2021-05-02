import AbstractView from "./AbstractView.js";
import HttpClient from "../HttpClient.js";
import Constants from "../Constants.js";

const ipUrl = new Constants().ipUrl;
const urlRaiz = new Constants().ipRaiz;
export default class extends AbstractView {
    constructor(params) {
        super(params);
        this.setTitle("Status");
        this.status = this.getStatus();
        this.table = this.loadTable();
    }

    async getHtml() {
        return `
        
        `;
    }

    async getStatus() {
        var status;

        var url = ipUrl + "/api/status";
        var client = new HttpClient();

        status = client.getSinc(url);
        var json = JSON.parse(status);
        console.log(JSON.stringify(json));
        return json;
    }

    getStatus() {
        var status;

        var url = ipUrl + "/api/status";
        var client = new HttpClient();

        status = client.getSinc(url);
        var json = JSON.parse(status);
        // console.log(JSON.stringify(json));
        return json;
    }

    async loadTable() {
        var json = this.getStatus();
        console.log(JSON.stringify(json));
        var tabledata = [];
        for (var i = 0; i < json.length; i++) {
            if (json[i].codStatus > 0) {
                tabledata[i] = {
                    codStatus: json[i].codStatus,
                    status: json[i].status,
                    excluir: "Excluir"
                }
            }
        }
        console.log(tabledata);
        var table = new Tabulator("#statusTable", {
            data: tabledata,
            //  selectable: true,
            layout: "fitDataFill",
            columns: [{
                    title: "ID",
                    field: "codStatus",
                    sorter: "number",
                    width: 50,
                    headerFilter: "number",
                    editor: false
                },
                { title: "Status", field: "status", sorter: "string", headerFilter: "input", hozAlign: "center", width: 200, editor: "input" },

                {
                    title: "Excluir",
                    field: "excluir",
                    cellClick: function(e, cell) {
                        var url = ipUrl + "/api/status?id=" + cell.getRow().getIndex();
                        var client = new HttpClient();
                        client.delSinc(url);
                        window.location.replace(urlRaiz + "/configs");
                    }
                }
            ],
            index: "codStatus",
            initialSort: [{ column: "codStatus", dir: "asc" }]

        });

        return table;
    }

    getTable() {
        return this.table;
    }

}