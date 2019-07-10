lista = document.getElementsByClassName('repo-list-item d-flex flex-column flex-md-row flex-justify-start py-4 public source');

function exportToTxt(dados, filename = '') {
    var downloadLink;
    var dataType = 'text/csv';
    
    // Specify file name
    filename = filename?filename+'.txt':'link_data.txt';
    
    // Create download link element
    downloadLink = document.createElement("a");
    
    document.body.appendChild(downloadLink);
    
    if(navigator.msSaveOrOpenBlob){
        var blob = new Blob(['\ufeff', dados.join('\n')], {
            type: dataType
        });
        navigator.msSaveOrOpenBlob( blob, filename);
    } else {
        // Create a link to the file
        downloadLink.href = 'data:' + dataType + ', ' + dados.join('\n');
    
        // Setting the file name
        downloadLink.download = filename;
        
        //triggering the function
        downloadLink.click();
    }
};

links = [];

for (let item of lista) {
  div = item.children[0];
  h3 = div.children[0];
  a = h3.children[0];
  links.push(a.href + '.git');
}

exportToTxt(links, 'links_projeto_java');
