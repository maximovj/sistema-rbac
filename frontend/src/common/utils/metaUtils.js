const HtmlMetaTitle = window.document.title;

export function setMetaTitleWelcome(page){
    window.document.title = HtmlMetaTitle + ' / ' + 'Bienvenido a ' + page;
}

export function setMetaTitle(page){
    window.document.title = HtmlMetaTitle + ' / ' + page;
}