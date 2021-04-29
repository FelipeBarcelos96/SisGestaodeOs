import Requisitos from "./views/Requisitos.js";
import Users from "./views/Users.js";
import User from "./views/User.js";
import Equipe from "./views/Equipe.js";
import Settings from "./views/Settings.js";
import HttpClient from "./HttpClient.js";
import UserCad from "./views/UserCad.js";
import Equipes from "./views/Equipes.js";

const pathToRegex = path => new RegExp("^" + path.replace(/\//g, "\\/").replace(/:\w+/g, "(.+)") + "$");

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

const navigateTo = url => {
    history.pushState(null, null, url);
    router();
};

const router = async() => {
    const routes = [
        { path: "/", view: Requisitos },
        { path: "/users", view: Users },
        { path: "/equips", view: Equipes },
        { path: "/users/cad", view: UserCad },
        { path: "/users/:id", view: User },
        { path: "/equips/:id", view: Equipe },
        { path: "/settings", view: Settings }
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

    if (match.route.path === "/users/cad") {
        document.getElementById("app").hidden = true;
        document.getElementById("cadForm").hidden = false;
        document.getElementById("users").hidden = true;

        document.querySelector("#equipSelect").innerHTML = await new Equipes().getOptionsHtml();

    } else if (match.route.path === "/users") {
        document.getElementById("app").hidden = false;
        document.getElementById("cadForm").hidden = true;
        document.getElementById("users").hidden = true;
    } else {
        document.getElementById("app").hidden = false;
        document.getElementById("cadForm").hidden = true;
        document.getElementById("users").hidden = true;
    }

    if (!match) {
        match = {
            route: routes[0],
            result: [location.pathname]
        };
    }

    const view = new match.route.view(getParams(match));

    document.querySelector("#app").innerHTML = await view.getHtml();
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

var userCad = document.getElementById("cad");
userCad.addEventListener("submit", (e) => {
    e.preventDefault();
    var json = {
        login: document.getElementById("name").value,
        password: document.getElementById("pass").value,
        ehAdm: document.getElementById("ehAdm").checked,
        ehGestor: document.getElementById("ehGestor").checked,
        ehDev: document.getElementById("ehDev").checked,
        ehAnal: document.getElementById("ehAnal").checked,
        equipe: new Equipe({
            id: document.getElementById("equipeselect").value
        }).getEquip()
    }
    const jsonStr = json;
    var url = "http://localhost:8080/api/users/register"
    const client = new HttpClient();
    /*
        var response = client.postAssincJson(url, jsonStr, function(response) {
            console.log(response);
        });
    */
    client.postAssincJson(url, jsonStr);
    //console.log(response);
    alert("Usuário Cadastrado com Sucesso!");
});