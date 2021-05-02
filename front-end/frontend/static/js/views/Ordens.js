import AbstractView from "./AbstractView.js";
import HttpClient from "../HttpClient.js";
import Constants from "../Constants.js";

const ipUrl = new Constants().ipUrl;
const urlRaiz = new Constants().ipRaiz;

export default class extends AbstractView {
    constructor(params) {
        super(params);
        this.params = params;
        this.setTitle("Ordens");
    }

    setTitle(title) {
        document.title = title;
    }

    async getHtml() {
        return `
        <h1>Ordens de Serviço</h1>
            <p>Ordens de Serviço ainda não foi implementado.</p>
            <p> Mas você pode ver <a href="/requisitos" data-link>Requisitos</a> no Lugar.</p>
        `;
    }
}