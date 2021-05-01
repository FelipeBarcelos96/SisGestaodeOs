import AbstractView from "./AbstractView.js";
import HttpClient from "../HttpClient.js";
import Equipe from "./Equipe.js";

export default class extends AbstractView {

    constructor(params) {
        super(params);
        this.userId = params.id;
        var user = this.getUser();
        this.nome = user.nome;
        this.pass = user.pass;
        this.ehAdm = user.ehAdm;
        this.ehGestor = user.ehGestor;
        this.ehDev = user.ehDev;
        this.ehAnal = user.ehAnal;
        if (user.equipe !== undefined) {
            this.equipe = new Equipe({ id: user.equipe.codEquipe });
        }
        this.setTitle("Usuario");
    }



    async getHtml() {
        var equipe;
        if (this.equipe !== undefined) {
            var url = "http://localhost:8180/equips/" + this.equipe.getId();
            equipe = `<p>Equipe: <a href="${url}">${this.equipe.getSigla()}</a></p>`
        } else {
            equipe = `<p>Equipe: Sem equipe</p>`;
        }

        var ehAdmin = this.ehAdm ? "checked" : "";
        var ehGestor = this.ehGestor ? "checked" : "";
        var ehDev = this.ehDev ? "checked" : "";
        var ehAnal = this.ehAnal ? "checked" : "";

        return `            
            <h1>${this.nome}</h1>
            <p>Você está vendo o Usuário de Id ${this.userId}.</p>
            <p>Usuario: ${this.nome} </p>
            <p hidden>Senha: ${this.pass} </p>
            <div>
            ${equipe}      
            </div>
            <div>
            <input type="checkbox" id="admin" name="admin" ${ehAdmin}>
            <label for="admin">Administrador</label>
            </div>
            <div>
            <input type="checkbox" id="gestor" name="gestor" ${ehGestor}>
            <label for="gestor">Gestor</label>
            </div>
            <div>
            <input type="checkbox" id="dev" name="dev" ${ehDev}>
            <label for="dev">Desenvolvedor</label>
            </div>
            <div>
            <input type="checkbox" id="anal" name="anal" ${ehAnal}>
            <label for="anal">Analista</label>
            </div>

            <button type="button" class="cancelbtn">Excluir</button>
        `;
    }

    getUser() {
        var url = "http://localhost:8080/api/users?id=" + this.userId;
        var user = new HttpClient().getSinc(url);
        return JSON.parse(user);
    }

    toString() {
        return JSON.stringify(this.getUser());
    }

}