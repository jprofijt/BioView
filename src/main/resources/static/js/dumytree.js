$("#tree").fancytree({
    source: [
        {title: "Node 1", key: "1"},
        {title: "Folder 2", key: "2", folder: true, children: [
                {title: "Node 2.1", key: "3", myOwnAttr: "abc"},
                {title: "Node 2.2", key: "4"}
            ]}
    ]
});