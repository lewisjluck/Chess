$(function() {
    var lastClicked = null;
    var possibleMoves = [];
    var clicked = null;
    var user = "white";

    $(".cell").click(function() {
        var coords = $(this).attr("id").substring(1);

        if (clicked != null) {
            if ($(clicked).hasClass("light")) {
                $(clicked).toggleClass("light-clicked");
            } else {
                $(clicked).toggleClass("dark-clicked");
            }
        }

        if ($(this).hasClass("light")) {
            $(this).toggleClass("light-clicked");
        } else {
            $(this).toggleClass("dark-clicked");
        }

        clicked = this;

        var moved = null;
        possibleMoves.forEach(cell => {
            $("#t" + cell["row"] + "-" + cell["column"] + " .empty").removeClass("circle-option");
            if (coords == cell["row"] + "-" + cell["column"]) {
                moved = cell;
            }
        })
        possibleMoves = [];

        if (moved) {
            $.ajax({
                url: "/move",
                data: { "from" : lastClicked,
                        "to" : coords,
                        "player" : user}
            });

            // move piece image to new location
            var image = $("#t" + lastClicked).html();
            $("#t" + lastClicked + " img").remove();
            $("#t" + lastClicked).html("<div class='empty'></div>");
            $(this).html(image);

            if (user == "white") {
                user = "black";
            } else {
                user = "white"
            }
            return;
        }

        $.ajax({
            url: "/get_moves",
            data: {"coords" : coords,
                   "player" : user}
        }).then(function(data) {
            console.log(data);
            for (var i = 0; i < data.length; i++) {
               $("#t" + data[i]["row"] + "-" + data[i]["column"] + " .empty").toggleClass("circle-option");
               possibleMoves.push(data[i]);
            }
        });

        lastClicked = coords;
    });
  });