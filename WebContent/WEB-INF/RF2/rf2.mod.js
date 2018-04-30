(function (ng) {
    var mod = angular.module('RF2Mod', ['ui.router']);

    mod.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

            $stateProvider.state('rf2', {
                views: {
                    inputView: {
                        templateUrl: "RF2/rf2.input.html",
                        controller: "RF2Ctrl"
                    }
                }
            }).state('rf2Response', {

                params: {
                    capacidad: null,
                    compartida: null,
                    tipoAlojamiento: null,
                    ubicacion: null,
                    numComparte: null,
                    operador: null
                },

                views: {
                    inputView: {
                        templateUrl: "RF2/rf2.input.html"
                    },
                    outputView: {
                        templateUrl: "RF2/rf2.output.html",
                        controller: "responseRF2Ctrl"
                    }
                }
            });
        }]);

})(window.angular);