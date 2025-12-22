const HtmlMetaTitle = window.document.title;
export function setTitle(page){
    window.document.title = HtmlMetaTitle + ' / ' + 'Bienvenido a ' + page;
}