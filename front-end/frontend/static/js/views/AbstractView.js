import Constants from "../Constants.js";

const ipUrl = new Constants().ipUrl;
const urlRaiz = new Constants().ipRaiz;
export default class {
    constructor(params) {
        this.params = params;
    }

    setTitle(title) {
        document.title = title;
    }

    async getHtml() {
        return "";
    }
}