import AbstractView from "./AbstractView.js";
import HttpClient from "../HttpClient.js";
import User from "./User.js";
import Equipes from "./Equipes.js";
import Constants from "../Constants.js";

const ipUrl = new Constants().ipUrl;
const urlRaiz = new Constants().ipRaiz;
export default class extends AbstractView {
    constructor(params) {
        super(params);
        this.setTitle("Usuarios");
        this.users = this.getUsersJson();
        this.table = this.loadTable();
    }

    async getHtml() {

        return `
        
        `;
    }

    getUsersHtml() {

        var json = this.getUsersJson();

        var dados = '';

        for (var i = 0; i < json.length; i++) {
            var ehAdm = (json[i].ehAdm ? 'Sim' : 'Não');
            var ehGestor = (json[i].ehGestor ? 'Sim' : 'Não');
            var ehDev = (json[i].ehDev ? 'Sim' : 'Não');
            var ehAnal = (json[i].ehAnal ? 'Sim' : 'Não');
            var url = urlRaiz + "/users/" + json[i].id;
            dados = dados + `<tr>
            <th><a href="${url}">${json[i].id}</a></th>
            <th>${json[i].nome}</th>
            <th>${ehAdm}</th>
            <th>${ehGestor}</th>
            <th>${ehDev}</th>
            <th>${ehAnal}</th>
          </tr>`
        }

        return dados;
    }

    getUsersJson() {
        var users;

        var url = ipUrl + "/api/users";
        var client = new HttpClient();

        users = client.getSinc(url);
        var json = JSON.parse(users);

        return json;
    }

    getTable() {
        return this.table;
    }

    async redirectToEquip(sigla) {
        var equipes = new Equipes().getEquips();
        var codEquipe;
        for (var i = 0; i < equipes.length; i++) {
            if (equipes[i].sigla === sigla) {
                id = equipes[i].codEquipe;
            }
        }
        if (codEquipe >= 0)
            return urlRaiz + "/equips/" + codEquipe;
        else
            return urlRaiz + "/users/";

    }

    async loadTable() {
        var json = this.getUsersJson();
        //console.log(JSON.stringify(json));
        var tabledata = [{}];
        for (var i = 0; i < json.length; i++) {
            tabledata[i] = {
                id: json[i].id,
                nome: json[i].nome,
                pass: json[i].pass,
                equipe: json[i].equipe.sigla,
                adiministrador: json[i].ehAdm,
                gestor: json[i].ehGestor,
                desenvolvedor: json[i].ehDev,
                analista: json[i].ehAnal,
                excluir: "Excluir"
            }

        }

        var equips = await new Equipes().getEquips();
        // console.log(JSON.stringify(equips));
        var equipsList = [];
        for (var i = 0; i < equips.length; i++) {
            equipsList[i] = equips[i].sigla;
        }

        //   console.log(equipsList);
        var table = new Tabulator("#usersTable", {
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
                        window.location.replace(urlRaiz + "/users/" + cell.getValue());
                    },
                },
                { title: "Nome", field: "nome", sorter: "string", headerFilter: "input", hozAlign: "center", width: 200, editor: "input" },
                {
                    title: "Equipe",
                    field: "equipe",
                    sorter: "string",
                    width: 100,
                    headerFilter: "input",
                    editor: "select",
                    editorParams: {
                        values: equipsList
                    }
                },
                { title: "Adiministrador", field: "adiministrador", sorter: "boolean", formatter: "tickCross", editor: "tickCross", hozAlign: "center", width: 150 },
                { title: "Gestor", field: "gestor", sorter: "boolean", formatter: "tickCross", hozAlign: "center", editor: "tickCross", width: 100 },
                { title: "Desenvolvedor", field: "desenvolvedor", sorter: "boolean", formatter: "tickCross", editor: "tickCross", hozAlign: "center", width: 150 },
                { title: "Analista", field: "analista", sorter: "boolean", formatter: "tickCross", editor: "tickCross", hozAlign: "center", width: 100 },
                {
                    title: "Excluir",
                    field: "excluir",
                    cellClick: function(e, cell) {
                        var url = ipUrl + "/api/users?id=" + cell.getRow().getIndex();
                        var client = new HttpClient();
                        client.delSinc(url);
                        window.location.replace(urlRaiz + "/users");
                    }
                }
            ],
            initialSort: [{ column: "id", dir: "asc" }]

        });

        return table;
    }
}