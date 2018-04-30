(function (ng) {
    var mod = angular.module('RF6Mod', ['ui.router']);

    mod.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

            $stateProvider.state('rf6', {
                views: {
                    inputView: {
                        templateUrl: "RF6/rf6.input.html",
                        controller: "RF6Ctrl"
                    }
                }
            }).state('rf6Response', {

                params: {
                	precio : null,
                	unidadDePrecio : null,
                	alojamiento : null,
                	fechaRetiro : null,
                	fechaCreacion: null
                },

                views: {
                    inputView: {
                        templateUrl: "RF6/rf6.input.html"
                    },
                    outputView: {
                        templateUrl: "RF6/rf6.output.html",
                        controller: "responseRF6Ctrl"
                    }
                }
            });
        }]);

})(window.angular);