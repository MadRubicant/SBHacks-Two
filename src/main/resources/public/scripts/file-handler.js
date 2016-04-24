var sendImageToBackend = function(encodedFile){
    $.post( "/identify", JSON.stringify(
        {
            file: encodedFile
        }),
    function( data ) {
        console.log(data);
        displayResults(data);
    },
    "json")
    .fail(function(err){
        console.log("Request failed.");
    });
};

var handleFileSelect = function(evt) {
    var files = evt.target.files;
    var file = files[0];

    if (files && file) {
        var reader = new FileReader();

        reader.onload = function(readerEvt) {
            var binaryString = readerEvt.target.result;
            sendImageToBackend(btoa(binaryString));
        };

        reader.readAsBinaryString(file);
    }
};

if (window.File && window.FileReader && window.FileList && window.Blob) {
    document.getElementById('filePicker').addEventListener('change', handleFileSelect, false);
} else {
    alert('The File APIs are not fully supported in this browser.');
}