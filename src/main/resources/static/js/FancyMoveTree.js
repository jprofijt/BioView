$(function () {
    $("#MoveTree").fancytree({
        source: [
            { title: "Node1", key: "Node1", folder: true },
            { title: "Node2", key: "Node2", folder: false },
            {
                title: "Node3",
                folder: true,
                children: [
                    { title: "Node3Child1", checkbox: false, folder: true },
                    { title: "Node3Child2", folder: false },
                    { title: "Node3Child3", folder: false }
                ]
            }
        ]
    });
    $(".fancytree-container").toggleClass("fancytree-connectors");
});

