import AbstractView from "./AbstractView.js";
import User from "./User.js";
import HttpClient from "../HttpClient.js";
import Constants from "../Constants.js";

const ipUrl = new Constants().ipUrl;
const urlRaiz = new Constants().ipRaiz;
export default class extends AbstractView {
    constructor(params) {
        super(params);
        this.setTitle("Requisito");
        //console.log(params.id);
        this.codReq = params.id;
        let req = this.getRequisito();
        this.analista = new User({ id: req.analista.id });
        this.titulo = req.titulo;
        this.descricao = req.descricao;
        this.prazo = req.prazo;
    }

    getRequisito() {
        let url = ipUrl + "/api/requisito?id=" + this.codReq;
        let req = new HttpClient().getSinc(url);
        return JSON.parse(req);
    }

    async getHtml() {
        return `
        `;
    }

    toString() {
        return JSON.stringify(this.getRequisito());
    }
}