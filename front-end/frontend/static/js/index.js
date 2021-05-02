import Requisitos from "./views/Requisitos.js";
import Requisito from "./views/Requisito.js";
import Users from "./views/Users.js";
import User from "./views/User.js";
import Equipe from "./views/Equipe.js";
import HttpClient from "./HttpClient.js";
import UserCad from "./views/UserCad.js";
import Equipes from "./views/Equipes.js";
import Status from "./views/Status.js";
import Ordens from "./views/Ordens.js";
import Prioridade from "./views/Prioridade.js";
import Constants from "./Constants.js"
import BemVindo from "./views/BemVindo.js";


const pathToRegex = path => new RegExp("^" + path.replace(/\//g, "\\/").replace(/:\w+/g, "(.+)") + "$");

const ipUrl = new Constants().getUrl();
const urlRaiz = new Constants().getUrlRaiz();
const getParams = match => {
    const values = match.result.slice(1);
    const keys = Array.from(match.route.path.matchAll(/:(\w+)/g)).map(result => result[1]);

    return Object.fromEntries(keys.map((key, i) => {
        return [key, values[i]];
    }));
};

const getHttpSinc = url => {
    return new HttpClient().getSinc(url);
};

const getEquipBySigla = sigla => {
    var equipes = new Equipes().getEquips();

    var codEquipe;
    for (var i = 0; i < equipes.length; i++) {
        if (equipes[i].sigla === sigla) {
            codEquipe = equipes[i].codEquipe;

        }
    }

    if (codEquipe >= 0)
        return codEquipe;
    else
        return 0;

};

const getUserByNome = nome => {
    let usuarios = new Users().getUsersJson();

    let id;
    var usuario;
    for (let i = 0; i < usuarios.length; i++) {
        if (usuarios[i].nome === nome) {
            usuario = {
                id: usuarios[i].id,
                login: usuarios[i].login,
                password: usuarios[i].password,
                ehAdm: usuarios[i].ehAdm,
                ehGestor: usuarios[i].ehGestor,
                ehDev: usuarios[i].ehDev,
                ehAnal: usuarios[i].ehAnal,
                equipe: usuarios[i].equipe
            }


        }
    }
    return usuario;
};

const navigateTo = url => {
    history.pushState(null, null, url);
    router();
};

var userLogado;

const router = async() => {
    const routes = [
        { path: "/", view: BemVindo },
        { path: "/login/:id", view: BemVindo },
        { path: "/users", view: Users },
        { path: "/ordens", view: Ordens },
        { path: "/equips", view: Equipes },
        { path: "/requisitos", view: Requisitos },
        { path: "/equips/cad", view: Equipes },
        { path: "/users/cad", view: UserCad },
        { path: "/requisitos/cad", view: Requisitos },
        { path: "/users/:id", view: User },
        { path: "/equips/:id", view: Equipe },
        { path: "/requisitos/:id", view: Requisito },
        { path: "/configs", view: Prioridade },
        { path: "/stats/cad", view: Status },
        { path: "/prio/cad", view: Prioridade }
    ];

    // Test each route for potential match
    const potentialMatches = routes.map(route => {
        return {
            route: route,
            result: location.pathname.match(pathToRegex(route.path))
        };
    });

    let match = potentialMatches.find(potentialMatch => potentialMatch.result !== null);

    console.log(match.route.path);

    if (!match) {
        match = {
            route: routes[0],
            result: [location.pathname]
        };
    }

    const view = new match.route.view(getParams(match));

    if (match.route.path === "/users/cad") {

        var equipsJson = await new Equipes().getEquips();
        var userEquipes = document.getElementById("cadUserEquip");
        for (var i = 0; i < equipsJson.length; i++) {
            var option = document.createElement("option");
            option.value = equipsJson[i].codEquipe;
            option.text = equipsJson[i].sigla;
            userEquipes.appendChild(option);
        }

        var userCad = document.getElementById("usercad");
        userCad.addEventListener("submit", (e) => {
            e.preventDefault();
            let json = {
                login: document.getElementById("name").value,
                password: document.getElementById("pass").value,
                ehAdm: document.getElementById("ehAdm").checked,
                ehGestor: document.getElementById("ehGestor").checked,
                ehDev: document.getElementById("ehDev").checked,
                ehAnal: document.getElementById("ehAnal").checked,
                equipe: new Equipe({
                    id: document.getElementById("cadUserEquip").value
                }).getEquip()
            }
            var url = ipUrl + "/api/users/register"
            var client = new HttpClient();
            /*
                var response = client.postAssincJson(url, jsonStr, function(response) {
                    console.log(response);
                });
            */
            client.postAssincJson(url, json);
            //console.log(response);

        });

        document.getElementById("app").hidden = true;
        document.getElementById("userCadForm").hidden = false;
        document.getElementById("users").hidden = true;
        document.getElementById("user").hidden = true;
        document.getElementById("equips").hidden = true;
        document.getElementById("equipCadForm").hidden = true;
        document.getElementById("equipe").hidden = true;
        document.getElementById("configs").hidden = true;
        document.getElementById("prioridadeCadForm").hidden = true;
        document.getElementById("requisitos").hidden = true;
        document.getElementById("requisitoCadForm").hidden = true;
        document.getElementById("requisito").hidden = true;
        //document.querySelector("#equipSelect").innerHTML = await new Equipes().getOptionsHtml();

    } else if (match.route.path === "/users") {

        var table = await view.getTable();
        var salvarUsuarios = document.getElementById("salvarUsuariosButton");
        salvarUsuarios.addEventListener("click", function() {
            var userData = table.getData();
            for (var i = 0; i < userData.length; i++) {
                //console.log(getEquipBySigla(userData[i].equipe));
                var json = {
                    id: userData[i].id,
                    equipe: { codEquipe: getEquipBySigla(userData[i].equipe), sigla: userData[i].equipe },
                    login: userData[i].nome,
                    password: userData[i].pass,
                    ehAdm: userData[i].adiministrador,
                    ehGestor: userData[i].gestor,
                    ehDev: userData[i].desenvolvedor,
                    ehAnal: userData[i].analista
                }

                new HttpClient().putSinc(ipUrl + "/api/users/register", json);
            }
            alert("Atualização efetuada com Sucesso");
        });

        document.getElementById("app").hidden = true;
        document.getElementById("userCadForm").hidden = true;
        document.getElementById("users").hidden = false;
        document.getElementById("user").hidden = true;
        document.getElementById("equips").hidden = true;
        document.getElementById("equipCadForm").hidden = true;
        document.getElementById("equipe").hidden = true;
        document.getElementById("configs").hidden = true;
        document.getElementById("statusCadForm").hidden = true;
        document.getElementById("prioridadeCadForm").hidden = true;
        document.getElementById("requisitos").hidden = true;
        document.getElementById("requisitoCadForm").hidden = true;
        document.getElementById("requisito").hidden = true;
    } else if (match.route.path === "/users/:id") {
        //document.querySelector("#user").innerHTML += await new User(getParams(match)).getHtml();
        var user = new User(getParams(match));
        document.getElementById("usuarioTitle").innerHTML = user.nome;
        document.getElementById("userId").innerHTML = "Você está vendo o Usuário de Id " + user.userId;
        document.getElementById("userNome").value = user.nome;
        document.getElementById("userPass").value = user.pass;
        document.getElementById("admin").checked = user.ehAdm;
        document.getElementById("gestor").checked = user.ehGestor;
        document.getElementById("dev").checked = user.ehDev;
        document.getElementById("anal").checked = user.ehAnal;
        var equipsJson = await new Equipes().getEquips();
        var userEquipes = document.getElementById("userEquip");
        for (var i = 0; i < equipsJson.length; i++) {
            var option = document.createElement("option");
            option.value = equipsJson[i].codEquipe;
            option.text = equipsJson[i].sigla;
            if (user.equipe.id === equipsJson[i].codEquipe) {
                option.selected = true;
            }
            userEquipes.appendChild(option);
        }
        var salvarUsuario = document.getElementById("salvarUsuarioButton");
        salvarUsuario.addEventListener("click", function() {
            let equip = document.getElementById("userEquip");

            var json = {
                id: user.userId,
                equipe: { codEquipe: equip.value, sigla: equip.options[equip.selectedIndex].text },
                login: document.getElementById("userNome").value,
                password: document.getElementById("userPass").value,
                ehAdm: document.getElementById("admin").checked,
                ehGestor: document.getElementById("gestor").checked,
                ehDev: document.getElementById("dev").checked,
                ehAnal: document.getElementById("anal").checked
            }

            new HttpClient().putSinc(ipUrl + "/api/users/register", json);

            alert("Atualização efetuada com Sucesso");
        });

        var excluiUsuario = document.getElementById("excluirUsuarioButton");
        excluiUsuario.addEventListener("click", function() {
            var url = ipUrl + "/api/users?id=" + user.userId;
            var client = new HttpClient();
            client.delSinc(url);
            window.location.replace(ipUrl + "/users");
        });

        document.getElementById("app").hidden = true;
        document.getElementById("userCadForm").hidden = true;
        document.getElementById("users").hidden = true;
        document.getElementById("user").hidden = false;
        document.getElementById("equips").hidden = true;
        document.getElementById("equipCadForm").hidden = true;
        document.getElementById("equipe").hidden = true;
        document.getElementById("configs").hidden = true;
        document.getElementById("statusCadForm").hidden = true;
        document.getElementById("prioridadeCadForm").hidden = true;
        document.getElementById("requisitos").hidden = true;
        document.getElementById("requisitoCadForm").hidden = true;
        document.getElementById("requisito").hidden = true;
    } else if (match.route.path === "/requisitos/:id") {
        //document.querySelector("#user").innerHTML += await new User(getParams(match)).getHtml();
        var req = new Requisito(getParams(match));
        document.getElementById("requisitoTitle").innerHTML = req.titulo;
        document.getElementById("requisitoId").innerHTML = "Você está vendo o Usuário de Id " + req.codReq;
        document.getElementById("requisitoTitulo").value = req.titulo;
        document.getElementById("requisitoDescricao").value = req.descricao;
        document.getElementById("requisitoPrazo").value = req.prazo;
        let usersJson = await new Users().getUsersJson();
        var reqUsers = document.getElementById("requisitoUser");
        for (let i = 0; i < usersJson.length; i++) {
            if (usersJson[i].ehAnal) {
                let option = document.createElement("option");
                option.value = usersJson[i].id;
                option.text = usersJson[i].nome;
                if (req.analista.userId === usersJson[i].id) {
                    option.selected = true;
                }
                reqUsers.appendChild(option);
            }
        }
        var salvarRequisito = document.getElementById("salvarRequisitoButton");
        salvarRequisito.addEventListener("click", function() {
            let user = document.getElementById("requisitoUser");
            let analistaUser = new User({
                id: user.value
            }).getUser();
            let json = {
                codReq: req.codReq,
                analista: {
                    id: analistaUser.id,
                    equipe: analistaUser.equipe,
                    login: analistaUser.nome,
                    password: analistaUser.pass,
                    ehAdm: analistaUser.ehAdm,
                    ehGestor: analistaUser.ehGestor,
                    ehDev: analistaUser.ehDev,
                    ehAnal: analistaUser.ehAnal
                },
                titulo: document.getElementById("requisitoTitulo").value,
                descricao: document.getElementById("requisitoDescricao").value,
                prazo: document.getElementById("requisitoPrazo").value
            }

            new HttpClient().putSinc(ipUrl + "/api/requisito/register", json);

            alert("Atualização efetuada com Sucesso");
        });

        var excluiRequisito = document.getElementById("excluirRequisitoButton");
        excluiRequisito.addEventListener("click", function() {
            var url = ipUrl + "/api/requisito?id=" + user.userId;
            var client = new HttpClient();
            client.delSinc(url);
            window.location.replace(ipUrl + "/requisitos");
        });

        document.getElementById("app").hidden = true;
        document.getElementById("userCadForm").hidden = true;
        document.getElementById("users").hidden = true;
        document.getElementById("user").hidden = true;
        document.getElementById("equips").hidden = true;
        document.getElementById("equipCadForm").hidden = true;
        document.getElementById("equipe").hidden = true;
        document.getElementById("configs").hidden = true;
        document.getElementById("statusCadForm").hidden = true;
        document.getElementById("prioridadeCadForm").hidden = true;
        document.getElementById("requisitos").hidden = true;
        document.getElementById("requisitoCadForm").hidden = true;
        document.getElementById("requisito").hidden = false;
    } else if (match.route.path === "/equips") {

        var table = await view.getTable();
        var salvarEquipes = document.getElementById("salvarEquipesButton");
        salvarEquipes.addEventListener("click", function() {
            var tableData = table.getData();
            for (var i = 0; i < tableData.length; i++) {

                var json = {
                    codEquipe: tableData[i].id,
                    sigla: tableData[i].sigla
                }

                new HttpClient().putSinc(ipUrl + "/api/equips/register", json);
            }
            alert("Atualização efetuada com Sucesso");
        });

        document.getElementById("app").hidden = true;
        document.getElementById("userCadForm").hidden = true;
        document.getElementById("users").hidden = true;
        document.getElementById("user").hidden = true;
        document.getElementById("equips").hidden = false;
        document.getElementById("equipCadForm").hidden = true;
        document.getElementById("equipe").hidden = true;
        document.getElementById("configs").hidden = true;
        document.getElementById("statusCadForm").hidden = true;
        document.getElementById("prioridadeCadForm").hidden = true;
        document.getElementById("requisitos").hidden = true;
        document.getElementById("requisitoCadForm").hidden = true;
        document.getElementById("requisito").hidden = true;
    } else if (match.route.path === "/equips/cad") {
        let equipCad = document.getElementById("equipCad");
        equipCad.addEventListener("submit", (e) => {
            e.preventDefault();
            let json = {
                sigla: document.getElementById("equipName").value
            }
            let url = ipUrl + "/api/equips/register"
            let client = new HttpClient();
            client.postAssincJson(url, json);
        });
        document.getElementById("app").hidden = true;
        document.getElementById("userCadForm").hidden = true;
        document.getElementById("users").hidden = true;
        document.getElementById("user").hidden = true;
        document.getElementById("equips").hidden = true;
        document.getElementById("equipCadForm").hidden = false;
        document.getElementById("equipe").hidden = true;
        document.getElementById("configs").hidden = true;
        document.getElementById("statusCadForm").hidden = true;
        document.getElementById("requisitos").hidden = true;
        document.getElementById("requisitoCadForm").hidden = true;
        document.getElementById("requisito").hidden = true;
    } else if (match.route.path === "/requisitos/cad") {
        let analistasJson = await new Users().getUsersJson();
        var analistas = document.getElementById("cadRequisitoUser");
        for (var i = 0; i < analistasJson.length; i++) {
            if (analistasJson[i].ehAnal) {
                let option = document.createElement("option");
                option.value = analistasJson[i].id;
                option.text = analistasJson[i].nome;
                analistas.appendChild(option);
            }
        }

        let requisitocad = document.getElementById("requisitocad");
        requisitocad.addEventListener("submit", (e) => {
            e.preventDefault();
            let analistaUser = new User({
                id: document.getElementById("cadRequisitoUser").value
            }).getUser();
            let json = {
                analista: {
                    id: analistaUser.id,
                    equipe: analistaUser.equipe,
                    login: analistaUser.nome,
                    password: analistaUser.pass,
                    ehAdm: analistaUser.ehAdm,
                    ehGestor: analistaUser.ehGestor,
                    ehDev: analistaUser.ehDev,
                    ehAnal: analistaUser.ehAnal
                },
                titulo: document.getElementById("requisitoCadTitulo").value,
                descricao: document.getElementById("requisitoCadDescricao").value,
                prazo: document.getElementById("requisitoCadPrazo").value
            }
            let url = ipUrl + "/api/requisito/register"
            let client = new HttpClient();
            client.postAssincJson(url, json);
        });
        document.getElementById("app").hidden = true;
        document.getElementById("userCadForm").hidden = true;
        document.getElementById("users").hidden = true;
        document.getElementById("user").hidden = true;
        document.getElementById("equips").hidden = true;
        document.getElementById("equipCadForm").hidden = true;
        document.getElementById("equipe").hidden = true;
        document.getElementById("configs").hidden = true;
        document.getElementById("statusCadForm").hidden = true;
        document.getElementById("requisitos").hidden = true;
        document.getElementById("requisitoCadForm").hidden = false;
        document.getElementById("requisito").hidden = true;
    } else if (match.route.path === "/equips/:id") {
        var equipe = new Equipe(getParams(match));
        document.getElementById("equipeTitle").innerHTML = equipe.sigla;
        document.getElementById("equipeId").innerHTML = "Você está vendo a Equipe de Id " + equipe.equipId;
        document.getElementById("equipeNome").value = equipe.sigla;
        await view.loadTable();

        var salvarEquipe = document.getElementById("salvarEquipeButton");
        salvarEquipe.addEventListener("click", function() {

            let json = {
                codEquipe: equipe.equipId,
                sigla: document.getElementById("equipeNome").value
            }

            new HttpClient().putSinc(ipUrl + "/api/equips/register", json);

            alert("Atualização efetuada com Sucesso");
        });

        var excluiEquipe = document.getElementById("excluirEquipeButton");
        excluiEquipe.addEventListener("click", function() {
            var url = ipUrl + "/api/equips?id=" + equipe.equipId;
            var client = new HttpClient();
            client.delSinc(url);
            window.location.replace("http://localhost:8180/equips");
        });

        document.getElementById("app").hidden = true;
        document.getElementById("userCadForm").hidden = true;
        document.getElementById("users").hidden = true;
        document.getElementById("user").hidden = true;
        document.getElementById("equips").hidden = true;
        document.getElementById("equipCadForm").hidden = true;
        document.getElementById("equipe").hidden = false;
        document.getElementById("configs").hidden = true;
        document.getElementById("statusCadForm").hidden = true;
        document.getElementById("prioridadeCadForm").hidden = true;
        document.getElementById("requisitos").hidden = true;
        document.getElementById("requisitoCadForm").hidden = true;
        document.getElementById("requisito").hidden = true;
    } else if (match.route.path === "/login/:id") {
        // userLogado = new User(getParams(match));
        localStorage.id = getParams(match).id;
        window.location.replace(urlRaiz);
    } else if (match.route.path === "/configs") {
        var statusTable = await new Status().loadTable();
        var prioridadeTable = await new Prioridade().loadTable();
        var salvarConfigs = document.getElementById("salvarConfigButton");
        salvarConfigs.addEventListener("click", function() {
            var statusTableData = statusTable.getData();
            for (var i = 0; i < statusTableData.length; i++) {

                var jsonStatus = {
                    codStatus: statusTableData[i].codStatus,
                    status: statusTableData[i].status
                }

                new HttpClient().putSinc(ipUrl + "/api/status/register", jsonStatus);
            }
            var prioridadeTableData = prioridadeTable.getData();
            for (var i = 0; i < prioridadeTableData.length; i++) {

                var jsonPrioridade = {
                    id: prioridadeTableData[i].id,
                    nome: prioridadeTableData[i].nome
                }

                new HttpClient().putSinc(ipUrl + "/api/prioridade/register", jsonPrioridade);
            }
            alert("Atualização efetuada com Sucesso");
        });
        document.getElementById("app").hidden = true;
        document.getElementById("userCadForm").hidden = true;
        document.getElementById("users").hidden = true;
        document.getElementById("user").hidden = true;
        document.getElementById("equips").hidden = true;
        document.getElementById("equipCadForm").hidden = true;
        document.getElementById("equipe").hidden = true;
        document.getElementById("configs").hidden = false;
        document.getElementById("statusCadForm").hidden = true;
        document.getElementById("prioridadeCadForm").hidden = true;
        document.getElementById("requisitos").hidden = true;
        document.getElementById("requisitoCadForm").hidden = true;
        document.getElementById("requisito").hidden = true;
    } else if (match.route.path === "/requisitos") {
        var requisitosTable = await new Requisitos().loadTable();
        var salvarRequisitos = document.getElementById("salvarRequisitosButton");
        salvarRequisitos.addEventListener("click", function() {
            var reqsData = requisitosTable.getData();
            for (let i = 0; i < reqsData.length; i++) {
                //console.log(getEquipBySigla(userData[i].equipe));
                let json = {
                    codReq: reqsData[i].codReq,
                    analista: getUserByNome(reqsData[i].analista),
                    titulo: reqsData[i].titulo,
                    descricao: reqsData[i].descricao,
                    prazo: reqsData[i].prazo
                }

                new HttpClient().putSinc(ipUrl + "/api/requisito/register", json);
            }
            alert("Atualização efetuada com Sucesso");
        });

        document.getElementById("app").hidden = true;
        document.getElementById("userCadForm").hidden = true;
        document.getElementById("users").hidden = true;
        document.getElementById("user").hidden = true;
        document.getElementById("equips").hidden = true;
        document.getElementById("equipCadForm").hidden = true;
        document.getElementById("equipe").hidden = true;
        document.getElementById("configs").hidden = true;
        document.getElementById("statusCadForm").hidden = true;
        document.getElementById("prioridadeCadForm").hidden = true;
        document.getElementById("requisitos").hidden = false;
        document.getElementById("requisitoCadForm").hidden = true;
        document.getElementById("requisito").hidden = true;
    } else if (match.route.path === "/stats/cad") {
        let statsCad = document.getElementById("statusCad");
        statsCad.addEventListener("submit", (e) => {
            e.preventDefault();
            let json = {
                status: document.getElementById("statusName").value
            }
            let url = ipUrl + "/api/status/register"
            let client = new HttpClient();
            client.postAssincJson(url, json);
        });
        document.getElementById("app").hidden = true;
        document.getElementById("userCadForm").hidden = true;
        document.getElementById("users").hidden = true;
        document.getElementById("user").hidden = true;
        document.getElementById("equips").hidden = true;
        document.getElementById("equipCadForm").hidden = true;
        document.getElementById("equipe").hidden = true;
        document.getElementById("configs").hidden = true;
        document.getElementById("statusCadForm").hidden = false;
        document.getElementById("prioridadeCadForm").hidden = true;
        document.getElementById("requisitos").hidden = true;
        document.getElementById("requisitoCadForm").hidden = true;
        document.getElementById("requisito").hidden = true;
    } else if (match.route.path === "/prio/cad") {
        let prioCad = document.getElementById("prioridadeCad");
        prioCad.addEventListener("submit", (e) => {
            e.preventDefault();
            let json = {
                nome: document.getElementById("prioridadeName").value
            }
            let url = ipUrl + "/api/prioridade/register"
            let client = new HttpClient();
            client.postAssincJson(url, json);
        });
        document.getElementById("app").hidden = true;
        document.getElementById("userCadForm").hidden = true;
        document.getElementById("users").hidden = true;
        document.getElementById("user").hidden = true;
        document.getElementById("equips").hidden = true;
        document.getElementById("equipCadForm").hidden = true;
        document.getElementById("equipe").hidden = true;
        document.getElementById("configs").hidden = true;
        document.getElementById("statusCadForm").hidden = true;
        document.getElementById("prioridadeCadForm").hidden = false;
        document.getElementById("requisitos").hidden = true;
        document.getElementById("requisitoCadForm").hidden = true;
        document.getElementById("requisito").hidden = true;
    } else {
        document.getElementById("app").hidden = false;
        document.getElementById("userCadForm").hidden = true;
        document.getElementById("users").hidden = true;
        document.getElementById("user").hidden = true;
        document.getElementById("equips").hidden = true;
        document.getElementById("equipCadForm").hidden = true;
        document.getElementById("equipe").hidden = true;
        document.getElementById("configs").hidden = true;
        document.getElementById("statusCadForm").hidden = true;
        document.getElementById("prioridadeCadForm").hidden = true;
        document.getElementById("requisitos").hidden = true;
        document.getElementById("requisitoCadForm").hidden = true;
        document.getElementById("requisito").hidden = true;
    }

    document.querySelector("#app").innerHTML = await view.getHtml();
    //console.log(localStorage.id);
};

window.addEventListener("popstate", router);
document.addEventListener("DOMContentLoaded", () => {
    document.body.addEventListener("click", e => {
        if (e.target.matches("[data-link]")) {
            e.preventDefault();
            navigateTo(e.target.href);
        }
    });
    router();
});