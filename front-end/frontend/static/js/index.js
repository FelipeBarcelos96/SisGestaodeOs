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
import Ordem from "./views/Ordem.js";


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

const getRequisitoByTitulo = titulo => {
    let requisitos = new Requisitos().getRequisitosJson();

    var requisito;
    for (let i = 0; i < requisitos.length; i++) {
        if (requisitos[i].titulo === titulo) {
            requisito = {
                codReq: requisitos[i].codReq,
                analista: {
                    id: requisitos[i].analista.id,
                    login: requisitos[i].analista.login,
                    password: requisitos[i].analista.password,
                    ehAdm: requisitos[i].analista.ehAdm,
                    ehGestor: requisitos[i].analista.ehGestor,
                    ehDev: requisitos[i].analista.ehDev,
                    ehAnal: requisitos[i].analista.ehAnal,
                    equipe: requisitos[i].analista.equipe
                },
                titulo: requisitos[i].titulo,
                descricao: requisitos[i].descricao,
                //prazo: requisitos[i].prazo
            }
        }
    }
    return requisito;
};

const getStastusByStatus = stat => {
    let statuss = new Status().getStatus();

    var status;
    for (let i = 0; i < statuss.length; i++) {
        if (statuss[i].status === stat) {
            status = {
                codStatus: statuss[i].codStatus,
                status: statuss[i].status
            }
        }
    }
    return status;
};

const getPrioridadeByNome = nome => {
    let prioridades = new Prioridade().getPrioridades();

    var prioridade;
    for (let i = 0; i < prioridades.length; i++) {
        if (prioridades[i].nome === nome) {
            prioridade = {
                id: prioridades[i].id,
                nome: prioridades[i].nome
            }
        }
    }
    return prioridade;
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
        { path: "/prio/cad", view: Prioridade },
        { path: "/ordens/cad", view: Ordens },
        { path: "/ordens/:id", view: Ordem }
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
        document.getElementById("ordens").hidden = true;
        document.getElementById("ordemCadForm").hidden = true;
        document.getElementById("ordem").hidden = true;
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
        document.getElementById("ordens").hidden = true;
        document.getElementById("ordemCadForm").hidden = true;
        document.getElementById("ordem").hidden = true;
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
            window.location.replace(urlRaiz + "/users");
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
        document.getElementById("ordens").hidden = true;
        document.getElementById("ordemCadForm").hidden = true;
        document.getElementById("ordem").hidden = true;
    } else if (match.route.path === "/requisitos/:id") {
        //document.querySelector("#user").innerHTML += await new User(getParams(match)).getHtml();
        var req = new Requisito(getParams(match));
        //console.log(req.prazo);
        document.getElementById("requisitoTitle").innerHTML = req.titulo;
        document.getElementById("requisitoId").innerHTML = "Você está vendo o Requisito de Id " + req.codReq;
        document.getElementById("requisitoTitulo").value = req.titulo;
        document.getElementById("requisitoDescricao").value = req.descricao;
        document.getElementById("requisitoPrazo").value = await new Constants().formatarDataForm(req.prazo);
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
                // console.log(document.getElementById("requisitoPrazo").value);
            new HttpClient().putSinc(ipUrl + "/api/requisito/register", json);

            alert("Atualização efetuada com Sucesso");
        });

        var excluiRequisito = document.getElementById("excluirRequisitoButton");
        excluiRequisito.addEventListener("click", function() {
            var url = ipUrl + "/api/requisito?id=" + req.codReq;
            var client = new HttpClient();
            client.delSinc(url);
            window.location.replace(urlRaiz + "/requisitos");
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
        document.getElementById("ordens").hidden = true;
        document.getElementById("ordemCadForm").hidden = true;
        document.getElementById("ordem").hidden = true;
    } else if (match.route.path === "/ordens/:id") {
        var ord = new Ordem(getParams(match));
        //console.log(req.prazo);
        document.getElementById("ordemId").innerHTML = "Você está vendo a Ordem de Serviço de Id " + ord.codOs;
        document.getElementById("ordemDescricao").value = ord.descricao;
        document.getElementById("ordemEsforco").value = ord.esforco;
        document.getElementById("ordemVlrEstimado").value = ord.vlrEstimado;
        document.getElementById("ordemEmissao").value = await new Constants().formatarDataForm(ord.emissao);
        document.getElementById("ordemConclusao").value = await new Constants().formatarDataForm(ord.entrega);
        let usersJson = await new Users().getUsersJson();
        var solicitanteUser = document.getElementById("ordemSolicitante");
        for (let i = 0; i < usersJson.length; i++) {
            let option = document.createElement("option");
            option.value = usersJson[i].id;
            option.text = usersJson[i].nome;
            if (ord.solicitante.userId === usersJson[i].id) {
                option.selected = true;
            }
            solicitanteUser.appendChild(option);
        }
        var encarregadoUser = document.getElementById("ordemEncarregado");
        for (let i = 0; i < usersJson.length; i++) {
            let option = document.createElement("option");
            option.value = usersJson[i].id;
            option.text = usersJson[i].nome;
            if (ord.encarregado.userId === usersJson[i].id) {
                option.selected = true;
            }
            encarregadoUser.appendChild(option);
        }
        let requsitosJson = await new Requisitos().getRequisitosJson();
        var ordemRequisito = document.getElementById("ordemRequisito");
        for (let i = 0; i < requsitosJson.length; i++) {
            let option = document.createElement("option");
            option.value = requsitosJson[i].codReq;
            option.text = requsitosJson[i].titulo;
            if (ord.requisito.codReq === requsitosJson[i].codReq) {
                option.selected = true;
            }
            ordemRequisito.appendChild(option);
        }
        let prioridadesJson = await new Prioridade().getPrioridades();
        var ordemPrioridade = document.getElementById("ordemPrioridade");
        for (let i = 0; i < prioridadesJson.length; i++) {
            let option = document.createElement("option");
            option.value = prioridadesJson[i].id;
            option.text = prioridadesJson[i].nome;
            if (ord.prioridade.id === prioridadesJson[i].id) {
                option.selected = true;
            }
            ordemPrioridade.appendChild(option);
        }
        let statusJson = await new Status().getStatus();
        var ordemStatus = document.getElementById("ordemStatus");
        for (let i = 0; i < statusJson.length; i++) {
            let option = document.createElement("option");
            option.value = statusJson[i].codStatus;
            option.text = statusJson[i].status;
            if (ord.status.codStatus === statusJson[i].codStatus) {
                option.selected = true;
            }
            ordemStatus.appendChild(option);
        }
        let equipesJson = await new Equipes().getEquips();
        var ordemEquipe = document.getElementById("ordemEquipe");
        for (let i = 0; i < equipesJson.length; i++) {
            let option = document.createElement("option");
            option.value = equipesJson[i].codEquipe;
            option.text = equipesJson[i].sigla;
            if (ord.equipe.id === equipesJson[i].codEquipe) {
                option.selected = true;
            }
            ordemEquipe.appendChild(option);
        }
        var salvarOrdem = document.getElementById("salvarOrdemButton");
        salvarOrdem.addEventListener("click", function() {
            let encarregado = document.getElementById("ordemEncarregado");
            let solicitante = document.getElementById("ordemSolicitante");
            let solicitanteUser = new User({
                id: solicitante.value
            }).getUser();
            let encarregadoUser = new User({
                id: encarregado.value
            }).getUser();

            let requisito = document.getElementById("ordemRequisito");
            let ordemRequisito = new Requisito({
                id: requisito.value
            }).getRequisito();

            let stat = document.getElementById("ordemStatus");
            let ordemPrio = document.getElementById("ordemPrioridade");
            let ordemEquipe = document.getElementById("ordemEquipe");
            let json = {
                    codOs: ord.codOs,
                    solicitante: {
                        id: solicitanteUser.id,
                        equipe: solicitanteUser.equipe,
                        login: solicitanteUser.nome,
                        password: solicitanteUser.pass,
                        ehAdm: solicitanteUser.ehAdm,
                        ehGestor: solicitanteUser.ehGestor,
                        ehDev: solicitanteUser.ehDev,
                        ehAnal: solicitanteUser.ehAnal
                    },
                    encarregado: {
                        id: encarregadoUser.id,
                        equipe: encarregadoUser.equipe,
                        login: encarregadoUser.nome,
                        password: encarregadoUser.pass,
                        ehAdm: encarregadoUser.ehAdm,
                        ehGestor: encarregadoUser.ehGestor,
                        ehDev: encarregadoUser.ehDev,
                        ehAnal: encarregadoUser.ehAnal
                    },
                    requisito: {
                        codReq: ordemRequisito.codReq,
                        analista: {
                            id: ordemRequisito.analista.id,
                            equipe: {
                                codEquipe: ordemRequisito.analista.equipe.id,
                                sigla: ordemRequisito.analista.equipe.sigla
                            },
                            login: ordemRequisito.analista.nome,
                            password: ordemRequisito.analista.pass,
                            ehAdm: ordemRequisito.analista.ehAdm,
                            ehGestor: ordemRequisito.analista.ehGestor,
                            ehDev: ordemRequisito.analista.ehDev,
                            ehAnal: ordemRequisito.analista.ehAnal
                        },
                        titulo: ordemRequisito.titulo,
                        descricao: ordemRequisito.descricao
                    },
                    status: {
                        codStatus: stat.value,
                        status: stat.text
                    },
                    prioridade: {
                        id: ordemPrio.value,
                        nome: ordemPrio.text
                    },
                    equipe: {
                        codEquipe: ordemEquipe.value,
                        sigla: ordemEquipe.text
                    },
                    emissao: document.getElementById("ordemEmissao").value,
                    descricao: document.getElementById("ordemDescricao").value,
                    esforco: document.getElementById("ordemEsforco").value,
                    entrega: document.getElementById("ordemConclusao").value,
                    vlrEstimado: document.getElementById("ordemVlrEstimado").value
                }
                // console.log(document.getElementById("requisitoPrazo").value);
            new HttpClient().putSinc(ipUrl + "/api/ordem/register", json);

            alert("Atualização efetuada com Sucesso");
        });

        var excluiOrdem = document.getElementById("excluirOrdemButton");
        excluiOrdem.addEventListener("click", function() {
            var url = ipUrl + "/api/ordem?id=" + ord.codOs;
            var client = new HttpClient();
            client.delSinc(url);
            window.location.replace(urlRaiz + "/ordens");
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
        document.getElementById("requisito").hidden = true;
        document.getElementById("ordens").hidden = true;
        document.getElementById("ordemCadForm").hidden = true;
        document.getElementById("ordem").hidden = false;
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
        document.getElementById("ordens").hidden = true;
        document.getElementById("ordemCadForm").hidden = true;
        document.getElementById("ordem").hidden = true;
    } else
    if (match.route.path === "/equips/cad") {
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
        document.getElementById("ordens").hidden = true;
        document.getElementById("ordemCadForm").hidden = true;
        document.getElementById("ordem").hidden = true;
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
            console.log(document.getElementById("requisitoCadPrazo").value);
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
        document.getElementById("ordens").hidden = true;
        document.getElementById("ordemCadForm").hidden = true;
        document.getElementById("ordem").hidden = true;
    } else if (match.route.path === "/ordens/cad") {

        let solicitantesJson = await new Users().getUsersJson();
        var solicitantes = document.getElementById("cadOrdemSolicitante");
        for (let i = 0; i < solicitantesJson.length; i++) {
            let option = document.createElement("option");
            option.value = solicitantesJson[i].id;
            option.text = solicitantesJson[i].nome;
            solicitantes.appendChild(option);
        }

        let encarregadosJson = await new Users().getUsersJson();
        var encarregados = document.getElementById("cadOrdemEncarregado");
        for (let i = 0; i < encarregadosJson.length; i++) {
            let option = document.createElement("option");
            option.value = encarregadosJson[i].id;
            option.text = encarregadosJson[i].nome;
            encarregados.appendChild(option);
        }

        let requisitosJson = await new Requisitos().getRequisitosJson();
        var requisitos = document.getElementById("cadOrdemRequisito");
        for (let i = 0; i < requisitosJson.length; i++) {
            let option = document.createElement("option");
            option.value = requisitosJson[i].codReq;
            option.text = requisitosJson[i].titulo;
            requisitos.appendChild(option);
        }

        let prioridadesJson = await new Prioridade().getPrioridades();
        var prioridades = document.getElementById("cadOrdemPrioridade");
        for (let i = 0; i < prioridadesJson.length; i++) {
            let option = document.createElement("option");
            option.value = prioridadesJson[i].id;
            option.text = prioridadesJson[i].nome;
            prioridades.appendChild(option);
        }

        let statusJson = await new Status().getStatus();
        var status = document.getElementById("cadOrdemStatus");
        for (let i = 0; i < statusJson.length; i++) {
            let option = document.createElement("option");
            option.value = statusJson[i].codStatus;
            option.text = statusJson[i].status;
            status.appendChild(option);
        }

        let equipsJson = await new Equipes().getEquips();
        var userEquipes = document.getElementById("cadOrdemEquipe");
        for (let i = 0; i < equipsJson.length; i++) {
            let option = document.createElement("option");
            option.value = equipsJson[i].codEquipe;
            option.text = equipsJson[i].sigla;
            userEquipes.appendChild(option);
        }

        let ordemCad = document.getElementById("ordemCad");
        ordemCad.addEventListener("submit", (e) => {
            e.preventDefault();
            let solicitanteUser = new User({
                id: document.getElementById("cadOrdemSolicitante").value
            }).getUser();
            let encarregadoUser = new User({
                id: document.getElementById("cadOrdemEncarregado").value
            }).getUser();
            let requisito = new Requisito({
                id: document.getElementById("cadOrdemRequisito").value
            });
            let prazo = new Constants().formatarDataForm(requisito.prazo);
            let status = document.getElementById("cadOrdemStatus");
            let prioridade = document.getElementById("cadOrdemPrioridade");

            let equipe = new Equipe({
                id: document.getElementById("cadOrdemEquipe").value
            });
            console.log(prazo);
            let json = {
                solicitante: {
                    id: solicitanteUser.id,
                    equipe: solicitanteUser.equipe,
                    login: solicitanteUser.nome,
                    password: solicitanteUser.pass,
                    ehAdm: solicitanteUser.ehAdm,
                    ehGestor: solicitanteUser.ehGestor,
                    ehDev: solicitanteUser.ehDev,
                    ehAnal: solicitanteUser.ehAnal
                },
                encarregado: {
                    id: encarregadoUser.id,
                    equipe: encarregadoUser.equipe,
                    login: encarregadoUser.nome,
                    password: encarregadoUser.pass,
                    ehAdm: encarregadoUser.ehAdm,
                    ehGestor: encarregadoUser.ehGestor,
                    ehDev: encarregadoUser.ehDev,
                    ehAnal: encarregadoUser.ehAnal
                },
                requisito: {
                    codReq: requisito.codReq,
                    analista: {
                        id: requisito.analista.id,
                        equipe: {
                            codEquipe: requisito.analista.equipe.id,
                            sigla: requisito.analista.equipe.sigla
                        },
                        login: requisito.analista.nome,
                        password: requisito.analista.pass,
                        ehAdm: requisito.analista.ehAdm,
                        ehGestor: requisito.analista.ehGestor,
                        ehDev: requisito.analista.ehDev,
                        ehAnal: requisito.analista.ehAnal
                    },
                    titulo: requisito.titulo,
                    descricao: requisito.descricao
                },
                status: {
                    codStatus: status.value,
                    status: status.text
                },
                prioridade: {
                    id: prioridade.value,
                    nome: prioridade.text
                },
                equipe: {
                    codEquipe: equipe.id,
                    sigla: equipe.sigla
                },
                emissao: document.getElementById("ordemCadEmissao").value,
                descricao: document.getElementById("ordemCadDescricao").value,
                esforco: document.getElementById("ordemCadEsforco").value,
                entrega: document.getElementById("ordemCadConclusao").value,
                vlrEstimado: document.getElementById("ordemCadVlrEstimado").value
            }

            let url = ipUrl + "/api/ordem/register"
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
        document.getElementById("requisitoCadForm").hidden = true;
        document.getElementById("requisito").hidden = true;
        document.getElementById("ordens").hidden = true;
        document.getElementById("ordemCadForm").hidden = false;
        document.getElementById("ordem").hidden = true;
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
            window.location.replace(urlRaiz + "/equips");
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
        document.getElementById("ordens").hidden = true;
        document.getElementById("ordemCadForm").hidden = true;
        document.getElementById("ordem").hidden = true;
    } else if (match.route.path === "/login/:id") {
        // userLogado = new User(getParams(match));
        // alert(getParams(match).id);
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
        document.getElementById("ordens").hidden = true;
        document.getElementById("ordemCadForm").hidden = true;
        document.getElementById("ordem").hidden = true;
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
        document.getElementById("ordens").hidden = true;
        document.getElementById("ordemCadForm").hidden = true;
        document.getElementById("ordem").hidden = true;
    } else if (match.route.path === "/ordens") {
        var ordensTable = await new Ordens().loadTable();
        var salvarOrdens = document.getElementById("salvarOrdensButton");
        salvarOrdens.addEventListener("click", function() {
            var ordsData = ordensTable.getData();
            for (let i = 0; i < ordsData.length; i++) {
                //console.log(getEquipBySigla(userData[i].equipe));
                let json = {
                    codOs: ordsData[i].codOs,
                    solicitante: getUserByNome(ordsData[i].solicitante),
                    encarregado: getUserByNome(ordsData[i].encarregado),
                    requisito: getRequisitoByTitulo(ordsData[i].requisito),
                    status: getStastusByStatus(ordsData[i].status),
                    prioridade: getPrioridadeByNome(ordsData[i].prioridade),
                    equipe: {
                        codEquipe: getEquipBySigla(ordsData[i].equipe),
                        sigla: ordsData[i].equipe
                    },
                    emissao: ordsData[i].emissao,
                    descricao: ordsData[i].descricao,
                    esforco: ordsData[i].esforco,
                    entrega: ordsData[i].entrega,
                    vlrEstimado: ordsData[i].vlrEstimado
                }

                new HttpClient().putSinc(ipUrl + "/api/ordem/register", json);
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
        document.getElementById("requisitos").hidden = true;
        document.getElementById("requisitoCadForm").hidden = true;
        document.getElementById("requisito").hidden = true;
        document.getElementById("ordens").hidden = false;
        document.getElementById("ordemCadForm").hidden = true;
        document.getElementById("ordem").hidden = true;
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
        document.getElementById("ordens").hidden = true;
        document.getElementById("ordemCadForm").hidden = true;
        document.getElementById("ordem").hidden = true;
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
        document.getElementById("ordens").hidden = true;
        document.getElementById("ordemCadForm").hidden = true;
        document.getElementById("ordem").hidden = true;
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
        document.getElementById("ordens").hidden = true;
        document.getElementById("ordemCadForm").hidden = true;
        document.getElementById("ordem").hidden = true;
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