import AbstractView from "./AbstractView.js";
import HttpClient from "../HttpClient.js";
import Constants from "../Constants.js";
import Requisitos from "./Requisitos.js";
import Status from "./Status.js";
import Prioridade from "./Prioridade.js";
import Users from "./Users.js";
import Equipes from "./Equipes.js";

const ipUrl = new Constants().ipUrl;
const urlRaiz = new Constants().ipRaiz;

export default class extends AbstractView {
    constructor(params) {
        super(params);
        this.params = params;
        this.setTitle("Ordens");
    }

    getOrdensJson() {
        let ords;
        let url = ipUrl + "/api/ordem";
        let client = new HttpClient();

        ords = client.getSinc(url);
        var json = JSON.parse(ords);

        return json;
    }

    async getHtml() {
        return `
        <h1>Ordens de Serviço</h1>
            <p>Ordens de Serviço ainda não foi implementado.</p>
            <p> Mas você pode ver <a href="/requisitos" data-link>Requisitos</a> no Lugar.</p>
        `;
    }

    async loadTable() {
        let json = this.getOrdensJson();
        console.log(JSON.stringify(json));
        var tabledata = [];

        for (let i = 0; i < json.length; i++) {

            tabledata[i] = {
                codOs: json[i].codOs,
                solicitante: json[i].solicitante.nome,
                encarregado: json[i].encarregado.nome,
                requisito: json[i].requisito.titulo,
                status: json[i].status.status,
                prioridade: json[i].prioridade.nome,
                equipe: json[i].equipe.sigla,
                emissao: await new Constants().formatarDataForm(json[i].emissao),
                descricao: json[i].descricao,
                esforco: json[i].esforco,
                entrega: await new Constants().formatarDataForm(json[i].entrega),
                vlrEstimado: json[i].vlrEstimado,
                excluir: "Excluir"
            }

        }


        let usuarios = await new Users().getUsersJson();

        var usuariosList = [];
        for (let i = 0; i < usuarios.length; i++) {
            usuariosList[i] = usuarios[i].nome;
        }

        let requisitos = await new Requisitos().getRequisitosJson();

        var requisitosList = [];
        for (let i = 0; i < requisitos.length; i++) {
            requisitosList[i] = requisitos[i].titulo;
        }

        var equips = await new Equipes().getEquips();

        var equipsList = [];
        for (let i = 0; i < equips.length; i++) {
            equipsList[i] = equips[i].sigla;
        }

        var status = await new Status().getStatus();

        var statusList = [];
        for (let i = 0; i < status.length; i++) {
            statusList[i] = status[i].status;
        }

        var prioridades = await new Prioridade().getPrioridades();

        var prioridadesList = [];
        for (let i = 0; i < prioridades.length; i++) {
            prioridadesList[i] = prioridades[i].nome;
        }

        var dateEditor = function(cell, onRendered, success, cancel) {
            //cell - the cell component for the editable cell
            //onRendered - function to call when the editor has been rendered
            //success - function to call to pass the successfuly updated value to Tabulator
            //cancel - function to call to abort the edit and return to a normal cell

            //create and style input
            var cellValue = moment(cell.getValue(), "DD/MM/YYYY hh:mm"),
                input = document.createElement("input");

            input.setAttribute("type", "date");

            input.style.padding = "4px";
            input.style.width = "100%";
            input.style.boxSizing = "border-box";

            input.value = cellValue;

            onRendered(function() {
                input.focus();
                input.style.height = "100%";
            });

            function onChange() {
                if (input.value != cellValue) {
                    success(moment(input.value, "YYYY-MM-DD hh:mm").format("DD/MM/YYYY hh:mm"));
                } else {
                    cancel();
                }
            }

            //submit new value on blur or change
            input.addEventListener("blur", onChange);

            //submit new value on enter
            input.addEventListener("keydown", function(e) {
                if (e.keyCode == 13) {
                    onChange();
                }

                if (e.keyCode == 27) {
                    cancel();
                }
            });

            return input;
        };


        //   console.log(equipsList);
        var table = new Tabulator("#ordensTable", {
            data: tabledata,
            //  selectable: true,
            layout: "fitColumns",
            columns: [{
                    title: "ID",
                    field: "codOs",
                    sorter: "number",
                    width: 50,
                    headerFilter: "number",
                    editor: false,
                    cellClick: function(e, cell) {
                        window.location.replace(urlRaiz + "/ordens/" + cell.getValue());
                    },
                },
                {
                    title: "Requisito",
                    field: "requisito",
                    sorter: "string",
                    width: 150,
                    headerFilter: "input",
                    editor: "select",
                    editorParams: {
                        values: requisitosList
                    }
                },
                {
                    title: "Prioridade",
                    field: "prioridade",
                    sorter: "string",
                    width: 120,
                    headerFilter: "input",
                    editor: "select",
                    editorParams: {
                        values: prioridadesList
                    }
                },
                {
                    title: "Solicitante",
                    field: "solicitante",
                    sorter: "string",
                    width: 150,
                    headerFilter: "input",
                    editor: "select",
                    editorParams: {
                        values: usuariosList
                    }
                },
                {
                    title: "Encarregado",
                    field: "encarregado",
                    sorter: "string",
                    width: 150,
                    headerFilter: "input",
                    editor: "select",
                    editorParams: {
                        values: usuariosList
                    }
                },
                {
                    title: "Status",
                    field: "status",
                    sorter: "string",
                    width: 100,
                    headerFilter: "input",
                    editor: "select",
                    editorParams: {
                        values: statusList
                    }
                },
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
                { title: "Descricao", field: "descricao", sorter: "string", formatter: "textarea", headerFilter: "input", hozAlign: "center", width: 200, editor: "input" },
                {
                    title: "Esforço",
                    field: "esforco",
                    sorter: "number",
                    width: 50,
                    headerFilter: "number",
                    editor: "number"
                },
                {
                    title: "Valor Estimado",
                    field: "vlrEstimado",
                    sorter: "number",
                    width: 50,
                    headerFilter: "number",
                    editor: "number"
                },
                {
                    title: "Emissao",
                    field: "emissao",
                    sorter: "datetime",
                    sorterParams: { format: "dd/mm/yyyy, hh/mm" },
                    hozAlign: "center",
                    width: 100,
                    formatter: "datetime"

                },
                {
                    title: "Conclusao",
                    field: "entrega",
                    sorter: "datetime",
                    sorterParams: { format: "dd/mm/yyyy, hh/mm" },
                    hozAlign: "center",
                    width: 100,
                    formatter: "datetime"

                },
                {
                    title: "Excluir",
                    field: "excluir",
                    cellClick: function(e, cell) {
                        var url = ipUrl + "/api/ordem?id=" + cell.getRow().getIndex();
                        var client = new HttpClient();
                        client.delSinc(url);
                        window.location.replace(urlRaiz + "/ordens");
                    }
                }
            ],
            index: "codOs",
            initialSort: [{ column: "codOs", dir: "asc" }]

        });

        return table;
    }

}