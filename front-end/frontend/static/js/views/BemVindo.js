import AbstractView from "./AbstractView.js";
import User from "./User.js";

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
                <a href="/users" data-link>Acessar Ordens de Serviço</a>.
            </p>
        `;
    }
}