import AbstractView from "./AbstractView.js";
import HttpClient from "../HttpClient.js";

export default class extends AbstractView {
    constructor(params) {
        super(params);
        this.setTitle("Equipes");
        this.equips = this.getEquips();
        this.table = this.loadTable();
    }

    async getHtml() {


        return `
        
        `;
    }

    async getOptionsHtml() {
        var json = this.getEquips();

        var dados = `<select id='equipeselect'>`;

        for (var i = 0; i < json.length; i++) {
            var id = json[i].codEquipe;
            var sigla = json[i].sigla;
            dados = dados + `
            <option value="${id}">${sigla}</option>            
            `
        }

        dados = dados + `</select>`

        return dados;
    }

    async getEquips() {
        var equips;

        var url = "http://localhost:8080/api/equips";
        var client = new HttpClient();

        equips = client.getSinc(url);
        var json = JSON.parse(equips);

        return json;
    }

    getEquips() {
        var equips;

        var url = "http://localhost:8080/api/equips";
        var client = new HttpClient();

        equips = client.getSinc(url);
        var json = JSON.parse(equips);

        return json;
    }

    async loadTable() {
        var json = this.getEquips();
        //console.log(JSON.stringify(json));
        var tabledata = [];
        for (var i = 0; i < json.length; i++) {
            if (json[i].codEquipe > 0) {
                tabledata[i] = {
                    id: json[i].codEquipe,
                    sigla: json[i].sigla,
                    excluir: "Excluir"
                }
            }
        }

        var table = new Tabulator("#equipsTable", {
            data: tabledata,
            //  selectable: true,
            layout: "fitDataFill",
            columns: [{
                    title: "ID",
                    field: "id",
                    sorter: "number",
                    width: 50,
                    headerFilter: "number",
                    editor: false,
                    cellClick: function(e, cell) {
                        window.location.replace("http://localhost:8180/equips/" + cell.getValue());
                    },
                },
                { title: "Equipe", field: "sigla", sorter: "string", headerFilter: "input", hozAlign: "center", width: 200, editor: "input" },

                {
                    title: "Excluir",
                    field: "excluir",
                    cellClick: function(e, cell) {
                        var url = "http://localhost:8080/api/equips?id=" + cell.getRow().getIndex();
                        var client = new HttpClient();
                        client.delSinc(url);
                        window.location.replace("http://localhost:8180/equips");
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