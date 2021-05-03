import AbstractView from "./AbstractView.js";
import Constants from "../Constants.js";
import HttpClient from "../HttpClient.js";
import Users from "./Users.js";

const ipUrl = new Constants().ipUrl;
const urlRaiz = new Constants().ipRaiz;
export default class extends AbstractView {
    constructor(params) {
        super(params);
        this.setTitle("Requisitos");
    }

    getRequisitosJson() {
        let reqs;
        let url = ipUrl + "/api/requisito";
        let client = new HttpClient();

        reqs = client.getSinc(url);
        var json = JSON.parse(reqs);

        return json;
    }

    async getHtml() {
        return `
        `;
    }

    async loadTable() {
        let json = this.getRequisitosJson();
        console.log(JSON.stringify(json));
        var tabledata = [];
        //   let a = [{ day: 'numeric' }, { month: 'numeric' }, { year: 'numeric' }];

        for (let i = 0; i < json.length; i++) {
            // let prazo = await new Constants().formatarData(json[i].prazo).then();
            tabledata[i] = {
                    codReq: json[i].codReq,
                    analista: json[i].analista.nome,
                    titulo: json[i].titulo,
                    descricao: json[i].descricao,
                    prazo: await new Constants().formatarDataForm(json[i].prazo), // prazo,
                    excluir: "Excluir"
                }
                // console.log(prazo);
        }


        let analistas = await new Users().getUsersJson();
        // console.log(JSON.stringify(equips));
        var analistasList = [];
        for (let i = 0; i < analistas.length; i++) {
            if (analistas[i].ehAnal)
                analistasList[i] = analistas[i].nome;
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
        var table = new Tabulator("#requisitosTable", {
            data: tabledata,
            //  selectable: true,
            layout: "fitColumns",
            columns: [{
                    title: "ID",
                    field: "codReq",
                    sorter: "number",
                    width: 50,
                    headerFilter: "number",
                    editor: false,
                    cellClick: function(e, cell) {
                        window.location.replace(urlRaiz + "/requisitos/" + cell.getValue());
                    },
                },
                { title: "Título", field: "titulo", sorter: "string", headerFilter: "input", hozAlign: "center", width: 200, editor: "input" },
                {
                    title: "Analista",
                    field: "analista",
                    sorter: "string",
                    width: 120,
                    headerFilter: "input",
                    editor: "select",
                    editorParams: {
                        values: analistasList
                    }
                },
                {
                    title: "Prazo",
                    field: "prazo",
                    sorter: "datetime",
                    sorterParams: { format: "dd/mm/yyyy, hh/mm" },
                    hozAlign: "center",
                    width: 100,
                    formatter: "datetime",
                    // editor: dateEditor,
                    /*
                    formatterParams: {
                        inputFormat: "d/M/yyyy HH:mm:ss",
                        outputFormat: "d/MM/yyyy HH:mm:ss",
                        invalidPlaceholder: "(invalid date)",
                        // timezone: "America/Sao_Paulo",
                    }
                   */
                },
                { title: "Descricao", field: "descricao", sorter: "string", formatter: "textarea", headerFilter: "input", hozAlign: "center", width: 200, editor: "input" },
                {
                    title: "Excluir",
                    field: "excluir",
                    cellClick: function(e, cell) {
                        var url = ipUrl + "/api/requisitos?id=" + cell.getRow().getIndex();
                        var client = new HttpClient();
                        client.delSinc(url);
                        window.location.replace(urlRaiz + "/requisitos");
                    }
                }
            ],
            index: "codReq",
            initialSort: [{ column: "codReq", dir: "asc" }]

        });

        return table;
    }
}