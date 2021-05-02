import AbstractView from "./AbstractView.js";
import User from "./User.js";
import Constants from "../Constants.js";

const ipUrl = new Constants().ipUrl;
const urlRaiz = new Constants().ipRaiz;
export default class extends AbstractView {
    constructor(params) {
        super(params);
        this.setTitle("Bem Vindo");
        this.userlogado = new User({ id: localStorage.id });
    }

    async getHtml() {
        return `
            <h1>Bem Vindo, ${this.userlogado.nome}</h1>

            <p>
                <a href="/ordens" data-link>Acessar Ordens de Serviço</a>.
            </p>
        `;
    }
}