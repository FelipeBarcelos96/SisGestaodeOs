import AbstractView from "./AbstractView.js";
import HttpClient from "../HttpClient.js";

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