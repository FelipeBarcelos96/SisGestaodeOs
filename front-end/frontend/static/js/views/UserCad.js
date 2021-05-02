import AbstractView from "./AbstractView.js";
import HttpClient from "../HttpClient.js";
import Constants from "../Constants.js";

const ipUrl = new Constants().ipUrl;
const urlRaiz = new Constants().ipRaiz;
export default class extends AbstractView {
    constructor(params) {
        super(params);
        this.userId = params.id;
        this.setTitle("Cadastro de Usuario");
    }

    async getHtml() {

        return `
            
                   
        `;
    }
}