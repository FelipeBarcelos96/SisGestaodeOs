import AbstractView from "./AbstractView.js";
import HttpClient from "../HttpClient.js";

export default class extends AbstractView {
    constructor(params) {
        super(params);
        this.setTitle("Usuarios");
    }

    async getHtml() {
        var dados = this.getUsers();

        return `
        <h1>Usuários</h1>        
        <table id="table" class="table" style="width:100%">
        <tr>
<th>ID</th>
<th>Nome</th>
<th>Administrador</th>
<th>Gestor</th>
<th>Desenvolvedor</th>
<th>Analista</th>
</tr>
${dados}
</table>
<br>
<button id="novoUsuarioButton" onclick="location.href = 'http://localhost:8180/users/cad';">Novo Usuario</button>
<button type="button" class="cancelbtn">Excluir</button>
        `;
    }

    getUsers() {
        var users;

        var url = "http://localhost:8080/api/users";
        var client = new HttpClient();

        users = client.getSinc(url);
        var json = JSON.parse(users);
        console.log(JSON.stringify(json));
        console.log(json.length);

        var dados = '';

        for (var i = 0; i < json.length; i++) {
            var ehAdm = (json[i].ehAdm ? 'Sim' : 'Não');
            var ehGestor = (json[i].ehGestor ? 'Sim' : 'Não');
            var ehDev = (json[i].ehDev ? 'Sim' : 'Não');
            var ehAnal = (json[i].ehAnal ? 'Sim' : 'Não');
            var url = "http://localhost:8180/users/" + json[i].id;
            dados = dados + `<tr>
            <th><a href="${url}">${json[i].id}</a></th>
            <th>${json[i].nome}</th>
            <th>${ehAdm}</th>
            <th>${ehGestor}</th>
            <th>${ehDev}</th>
            <th>${ehAnal}</th>
          </tr>`
        }

        return dados;
    }


}