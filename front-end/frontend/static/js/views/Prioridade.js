import AbstractView from "./AbstractView.js";
import HttpClient from "../HttpClient.js";

export default class extends AbstractView {
    constructor(params) {
        super(params);
        this.setTitle("Prioridade");
        this.prioridades = this.getPrioridades();
        this.table = this.loadTable();
    }

    async getHtml() {


        return `
        
        `;
    }

    async getPrioridades() {
        var prioridades;

        var url = "http://localhost:8080/api/prioridade";
        var client = new HttpClient();

        prioridades = client.getSinc(url);
        var json = JSON.parse(prioridades);
        //console.log(JSON.stringify(json));
        return json;
    }

    getPrioridades() {
        var prioridades;

        var url = "http://localhost:8080/api/prioridade";
        var client = new HttpClient();

        prioridades = client.getSinc(url);
        var json = JSON.parse(prioridades);
        //console.log(JSON.stringify(json));
        return json;
    }

    async loadTable() {
        var json = this.getPrioridades();
        console.log(JSON.stringify(json));
        var tabledata = [];
        for (var i = 0; i < json.length; i++) {
            if (json[i].id > 0) {
                tabledata[i] = {
                    id: json[i].id,
                    nome: json[i].nome,
                    excluir: "Excluir"
                }
            }
        }

        var table = new Tabulator("#prioridadesTable", {
            data: tabledata,
            //  selectable: true,
            layout: "fitDataFill",
            columns: [{
                    title: "ID",
                    field: "id",
                    sorter: "number",
                    width: 50,
                    headerFilter: "number",
                    editor: false
                },
                { title: "Prioridade", field: "nome", sorter: "string", headerFilter: "input", hozAlign: "center", width: 200, editor: "input" },

                {
                    title: "Excluir",
                    field: "excluir",
                    cellClick: function(e, cell) {
                        var url = "http://localhost:8080/api/prioridade?id=" + cell.getRow().getIndex();
                        var client = new HttpClient();
                        client.delSinc(url);
                        window.location.replace("http://localhost:8180/configs");
                    }
                }
            ],
            initialSort: [{ column: "id", dir: "asc" }]

        });

        return table;
    }

    getTable() {
        return this.table;
    }

}