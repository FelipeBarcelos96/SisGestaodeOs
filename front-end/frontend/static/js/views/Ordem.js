import AbstractView from "./AbstractView.js";
import User from "./User.js";
import Equipe from "./Equipe.js";
import Requisito from "./Requisito.js";
import HttpClient from "../HttpClient.js";
import Constants from "../Constants.js";

const ipUrl = new Constants().ipUrl;
const urlRaiz = new Constants().ipRaiz;
export default class extends AbstractView {
    constructor(params) {
        super(params);
        this.setTitle("Ordem de Serviço");
        this.codOs = params.id;
        var ord = this.getOrdem();
        this.solicitante = new User({ id: ord.solicitante.id });
        this.encarregado = new User({ id: ord.encarregado.id });
        this.requisito = new Requisito({ id: ord.requisito.id });
        this.status = ord.status;
        this.prioridade = ord.prioridade;
        this.equipe = new Equipe({ id: ord.equipe.id });
        this.emissao = ord.emissao;
        this.descricao = ord.descricao;
        this.esforco = ord.esforco;
        this.entrega = ord.entrega;
        this.vlrEstimado = ord.vlrEstimado;
    }

    getOrdem() {
        let url = ipUrl + "/api/ordem?id=" + this.codOs;
        let ord = new HttpClient().getSinc(url);
        return JSON.parse(ord);
    }

    async getHtml() {
        return `
        `;
    }

    toString() {
        return JSON.stringify(this.getOrdem());
    }
}