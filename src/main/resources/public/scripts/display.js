function displayResults(data){

    for(result in data.possible_pills){
        $("#results").append($("<li class='col-md-3'><a><img src='"+data.possible_images[result]
        +"'></a><p>"+data.possible_pills[result]+"</p></li>"));
    }
}