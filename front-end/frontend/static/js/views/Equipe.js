import AbstractView from "./AbstractView.js";
import HttpClient from "../HttpClient.js";
import Equipes from "./Equipes.js";
import Users from "./Users.js";

export default class extends AbstractView {

    constructor(params) {
        super(params);
        this.equipId = params.id;
        this.setTitle("Equipe");
        var equip = this.getEquip();
        this.id = equip.codEquipe;
        this.sigla = equip.sigla;
    }

    async getHtml() {
        return `            
            <h1>${this.sigla}</h1>
            <p>Você está vendo a Equipe de Id ${this.id}.</p>            
        `;
    }


    getEquip() {
        var equip;

        var url = "http://localhost:8080/api/equips?id=" + this.equipId;
        var client = new HttpClient();

        equip = client.getSinc(url);
        return JSON.parse(equip);
    }

    async getEquipBySigla(sigla) {
        var equipes = new Equipes().getEquips();
        var codEquipe;
        for (var i = 0; i < equipes.length; i++) {
            if (equipes[i].sigla === sigla) {
                codEquipe = equipes[i].codEquipe;
            }
        }
        if (codEquipe >= 0)
            codEquipe;
        else
            return 0;

    }

    getId() {
        return this.id;
    }

    getSigla() {
        return this.sigla;
    }

    loadTable() {
        var json = new Users().getUsersJson();
        //console.log(JSON.stringify(json));
        var tabledata = [];
        for (var i = 0; i < json.length; i++) {
            if (json[i].equipe.codEquipe === this.id) {
                tabledata[i] = {
                    id: json[i].id,
                    nome: json[i].nome
                }
            }
        }

        var table = new Tabulator("#equipeUsersTable", {
            data: tabledata,
            //  selectable: true,
            layout: "fitData",
            columns: [{
                    title: "ID",
                    field: "id",
                    sorter: "number",
                    width: 50,
                    headerFilter: "number",
                    editor: false,
                    cellClick: function(e, cell) {
                        window.location.replace("http://localhost:8180/users/" + cell.getValue());
                    },
                },
                { title: "Nome", field: "nome", sorter: "string", headerFilter: "input", hozAlign: "center", width: 200, editor: false }
            ],
            initialSort: [{ column: "id", dir: "asc" }]

        });

        return table;
    }

}