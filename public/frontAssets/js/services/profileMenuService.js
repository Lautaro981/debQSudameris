debFrontApp.factory('profileMenuService', [function () {
    var newMenu = {
        refactorMenu: function (menu) {

            // Todas las categorias
            var categories = [];
            // Menu refactor
            var menuRefactor = [];
            
            // Chequea si existe la categoria
            var checkIfExist = function (value) {
                // Booleano que compara
                var exist = true;
                // Recorro categorias
                for (var z = 0; z < categories.length; z++) {
                    // Si existe devuelve false
                    if (categories[z].name == value) {
                        exist = false;
                    }
                }
                return exist;
            };

            // Recorro el menu original
            angular.forEach(menu, function(menuItem) {
                if(menuItem.attribute.isDisplayed){
                    // Categoria de la posición actual
                    var actualCategory = menuItem.attribute.groupName;

                    // Si la categoria esta repetida o no
                    var isNew = checkIfExist(actualCategory);

                    // Si es una nueva, construyo el objeto;
                    if (isNew) {
                        categories.push({
                            'name' : actualCategory, 
                            'label' : menuItem.attribute.groupLabel, 
                            'icon' : menuItem.attribute.groupIcon, 
                            'categories' : []
                        });
                    }

                    // Agrega items a cada categoria
                    angular.forEach(categories, function(cat) {
                        if(cat.name == actualCategory) {
                            cat.categories.push(menuItem);
                        }
                    });
                }
            });

            // Menu a devolver
            return categories;
        }
    };
    return newMenu;
}]);