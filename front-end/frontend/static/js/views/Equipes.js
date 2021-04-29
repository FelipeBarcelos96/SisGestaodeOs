import AbstractView from "./AbstractView.js";
import HttpClient from "../HttpClient.js";

export default class extends AbstractView {
    constructor(params) {
        super(params);
        this.setTitle("Equipes");
    }

    async getHtml() {
        var json = this.getEquips();

        var dados = '';

        for (var i = 0; i < json.length; i++) {
            var id = json[i].codEquipe;
            var sigla = json[i].sigla;
            var url = "http://localhost:8180/equips/" + json[i].codEquipe;
            dados = dados + `
            <a href="${url}">${id}</a>    <a>${sigla}</a> 
            <br>
            `
        }

        return `
        <h1>Equipes</h1> 
        <b>ID              Sigla</b>   
        <br>            
          ${dados}
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

    getEquips() {
        var equips;

        var url = "http://localhost:8080/api/equips";
        var client = new HttpClient();

        equips = client.getSinc(url);
        var json = JSON.parse(equips);

        return json;
    }

}