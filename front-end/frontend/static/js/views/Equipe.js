import AbstractView from "./AbstractView.js";
import HttpClient from "../HttpClient.js";

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

    getId() {
        return this.id;
    }

    getSigla() {
        return this.sigla;
    }

}