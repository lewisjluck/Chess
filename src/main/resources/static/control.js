function presentMessage(message) {
  $("#overlay").css("display", "flex");
  $("#overlay").append("<span id=\"message\">" + message + "<br> <a href=\"play\">Play Again?</a></span>");
}

$(function() {
    var lastClicked = null;
    var possibleMoves = [];
    var clicked = null;
    var user = "white";

    // when a cell is clicked
    $(".cell").click(function() {
        // access coordinates of clicked cell
        var coords = $(this).attr("id")

        // another cell has been previously clicked - need to clear highlighting
        if (clicked != null) {
            if ($(clicked).hasClass("light")) {
                $(clicked).toggleClass("light-clicked");
            } else {
                $(clicked).toggleClass("dark-clicked");
            }
        }

        // add highlighting for clicked cell
        if ($(this).hasClass("light")) {
            $(this).toggleClass("light-clicked");
        } else {
            $(this).toggleClass("dark-clicked");
        }

        // store clicked cell for clearing or moving next click
        clicked = this;

        // remove style from previous click's possible moves
        var moved = null;
        possibleMoves.forEach(cell => {
            $("#" + cell + " .empty").removeClass("circle-option");
            $("#" + cell + " img").removeClass("piece-option");

            // this click is a previous'c click possible move -> move has occurred
            if (coords == cell) {
                moved = cell;
            }
        });

        possibleMoves = [];

        if (moved) {
            // move piece image to new location
            var image = $("#" + lastClicked).html();
            $("#" + lastClicked + " img").remove();
            $("#" + lastClicked).html("<div class='empty'></div>");
            $(this).html(image);

            $.ajax({
                url: "/move",
                data: { "from" : lastClicked,
                        "to" : coords,
                        "player" : user},
                success:
                function (data) {
                    if (data["gameOver"] == "true") {
                        ;
                        //presentMessage(data["gameOver"], data["winner"], data["loser"])
                    } else {
                        for (var i = 0; i < data["display"].length; i++) {
                            var display = data["display"][i];
                            var position = data["position"][i];
                            if (display == "NONE") {
                                $("#" + position).html("<div class='empty'></div>");
                            } else {
                                $("#" + position).html("<img class='piece' src='" + display + "'>");
                            }
                        }

                        $.ajax({
                                        url: "/get_engine_move",
                                        data: { "elo" : "2000",
                                                "player" : "black"},
                                        success:
                                        function (data) {
                                            if (data["gameOver"] == "true") {
                                                ;
                                                //presentMessage(data["gameOver"], data["winner"], data["loser"])
                                            } else {
                                                for (var i = 0; i < data["display"].length; i++) {
                                                    var display = data["display"][i];
                                                    var position = data["position"][i];
                                                    if (display == "NONE") {
                                                        $("#" + position).html("<div class='empty'></div>");
                                                    } else {
                                                        $("#" + position).html("<img class='piece' src='" + display + "'>");
                                                    }
                                                }
                                            }
                                        }
                                    });
                    }
                }
            });

            /*
            if (user == "white") {
                user = "black";
            } else {
                user = "white"
            }
            */




            return;
        }

        // each turn fetch moves from clicked square
        $.ajax({
            url: "/get_moves",
            data: {"coords" : coords,
                   "player" : user}
        }).then(function(data) {
            data.forEach(cell => {
                $("#" + cell + " .empty").addClass("circle-option");
                $("#" + cell + " img").addClass("piece-option")
                possibleMoves.push(cell);
            });
        });

        lastClicked = coords;
    });
  });