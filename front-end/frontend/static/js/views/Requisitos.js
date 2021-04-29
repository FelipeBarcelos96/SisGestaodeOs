import AbstractView from "./AbstractView.js";

export default class extends AbstractView {
    constructor(params) {
        super(params);
        this.setTitle("Requisitos");
    }

    async getHtml() {
        return `
            <h1>Bem Vindo, Administrador</h1>

            <p>
                <a href="/users" data-link>Acessar Ordens de Serviço</a>.
            </p>
        `;
    }
}