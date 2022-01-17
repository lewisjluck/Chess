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
            $("#t" + cell["row"] + "-" + cell["column"] + " img").removeClass("piece-option");
            if (coords == cell["row"] + "-" + cell["column"]) {
                moved = cell;
            }
        });

        possibleMoves = [];

        if (moved) {
            // move piece image to new location
            var image = $("#t" + lastClicked).html();
            $("#t" + lastClicked + " img").remove();
            $("#t" + lastClicked).html("<div class='empty'></div>");
            $(this).html(image);

            $.ajax({
                url: "/move",
                data: { "from" : lastClicked,
                        "to" : coords,
                        "player" : user},
                success:
                function (data) {
                    for (var i = 0; i < data["display"].length; i++) {
                        var display = data["display"][i];
                        var position = data["position"][i];
                        if (display == "NONE") {
                            $("#t" + position).html("<div class='empty'></div>");
                        } else {
                            $("#t" + position).html("<img class='piece' src='" + display + "'>");
                        }
                    }
                }
            });

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
               $("#t" + data[i]["row"] + "-" + data[i]["column"] + " .empty").addClass("circle-option");
               $("#t" + data[i]["row"] + "-" + data[i]["column"] + " img").addClass("piece-option")
               possibleMoves.push(data[i]);
            }
        });

        lastClicked = coords;
    });
  });