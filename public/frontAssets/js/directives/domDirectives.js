var domDirectives = angular.module('domDirectives', []);

domDirectives.directive('mainMenu', function ($document) {
    return {
        restrict: 'EA',
        transclude: true,
        templateUrl: '/assets/frontAssets/templates/mainMenu.html',
        scope: {
            data: '=',
            path: '@',
            func: '='
        },
        link: function (scope, element, attr) {
            // Watcher al profileMenu
            scope.$watch('data', function (value) {
                $('.js-item').click(function (event) {
                    // Marco el grupo seleccionado
                    if(!$(this).hasClass('main-navbar__item--active')){
                        $(this).addClass('main-navbar__item--active');
                        $('.main-navbar__item').not(this).removeClass('main-navbar__item--active');
                    }
                    // Si hago click en el submenu, no hace slideUp
                    $(this).find('.js-submenu').click(function(){
                        return false;
                    });

                    // SlideToggle si clickeo en los titulos
                    $(this).find('.js-submenu').slideToggle();
                    $('.js-submenu').not($(this).find('.js-submenu')).slideUp();
                });
            });

            // Marco Menu seleccionado
            scope.itemClicked = function (index, parentIndex, event) {
                scope.selectedIndex = index;
                scope.selectedParentIndex = parentIndex;
            };

            // Broadcast hacia controller 
            // Cambio de categoria externo al menu
            scope.newCategory = function (category) {
                scope.$parent.$broadcast("newCategory", {
                    "url": category.attribute.url
                });
            };
            
            scope.$on("changeCategory", function (event, args) {
                var categoryName = args.categoryName;
                for (var i = 0; i < scope.data.length; i++) {
                    for (var j = 0; j < scope.data[i].categories.length; j++) {
                        if (scope.data[i].categories[j].name === categoryName) {
                            //Item seleccionado
                            scope.itemClicked(j, i);
                            //Cambio de categoría
                            scope.newCategory({
                                data: scope.data[i].categories[j]
                            });
                            // Accordion Menu
                            var li = $('.' + scope.data[i].name);
                            li.find('.js-submenu').slideDown();
                            $('.js-submenu').not(li.find('.js-submenu')).slideUp();
                        }
                    }
                }
            });
        }
    };
});

domDirectives.directive('dashConfig',['$document',  function ($document) {
    return {
        restrict: 'EA',
        replace: false,
        link: function (scope, element) {
            var configPopup = $(element).find('.js-popup');
            $document.bind('click', function(event){
                var isClickedElementChildOfPopup = $(element)
                  .find(event.target)
                  .length > 0;
                if (isClickedElementChildOfPopup){
                    // CLick Adentro de la directiva
                    if($(event.target).hasClass('js-edit')){
                        configPopup.toggleClass('show');
                    }
                    return false;
                }            
                // Clicks afuera de la directiva
                if(configPopup.hasClass('show')){
                    configPopup.removeClass('show');
                }

                scope.$apply();
            });
        }
    };
}]);

domDirectives.directive('mobileMenu',['$document',  function ($document) {
    return {
        restrict: 'EA',
        replace: false,
        link: function (scope, element) {
            var menu = $($document).find('.js-main-menu');
            // Detecto clicks en el documento
            $document.bind('click', function(event){
                var isClickedElementChildOfDirective = $(element)
                  .find(event.target)
                  .length > 0;
                if (isClickedElementChildOfDirective){
                    // Adentro de la directiva
                    element.toggleClass('on');
                    menu.toggleClass('show');                    
                    return false;
                }
                if($(event.target).closest('.js-main-menu').length>0){
                    // Evito el cierre si clickeo en el menu
                    return false;
                }    
                // Cierro menu al clickear afuera
                if(menu.hasClass('show')){
                    menu.removeClass('show');
                    element.removeClass('on');
                }
                scope.$apply();
            });
            
        }
    };
}]);

domDirectives.directive('userSubmenu', function () {
    return {
        restrict: 'EA',
        templateUrl: 'assets/debFront/assets/templates/userSubmenu.html',
        transclude: true,
        replace: true,
        link: function (scope, element, attr) {
            //Para hacerlo así falta detectar el click afuera
            var subMenu = element.find('.submenu-usuarios');
            var fade = false;
            // DropDown User Menu
            element.click(function (event) {
                event.stopPropagation();
                if (!fade) {
                    subMenu.fadeIn(300);
                    fade = true;
                    element.find('p').css('color', '#ffffff');
                    element.find('span').css('color', '#ffffff');
                } else {
                    subMenu.fadeOut(200);
                    element.find('p').css('color', '#a4abb3');
                    element.find('span').css('color', '#a4abb3');
                    fade = false;
                }
            });
            angular.element(document).bind('click', function () {
                subMenu.fadeOut(200);
                element.find('p').css('color', '#a4abb3');
                element.find('span').css('color', '#a4abb3');
                fade = false;
            });

        }
    };
});

domDirectives.directive('collapse', function(){
    return {
        restrict: 'A',
        scope: {
            collapse: '=',
            parent: '='
        },
        link: function(scope, element, attr) {
            var $target = null;
            var $parent = null;
            if (scope.parent) {
                $parent = $(scope.parent)
            }
            if (scope.collapse) {
                // Por alguna razon cuando llega a este punto el elemneto a buscar no existe todavia :(
                $target = $(scope.collapse);
                $(element).on('click', function(e){
                    e.preventDefault();
                    e.stopPropagation();
                    // Solucion, siempre buscarlo con cada click.
                    $target = $(scope.collapse);
                    if ($parent) {
                        $parent.find('.collapse-deb').not($target).slideUp().removeClass('collapse-deb');
                    }
                    $target.slideToggle();
                    $target.addClass('collapse-deb');
                });
            }
        }
    }
});