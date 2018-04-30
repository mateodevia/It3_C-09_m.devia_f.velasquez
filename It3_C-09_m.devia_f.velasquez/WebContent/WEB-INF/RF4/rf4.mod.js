(function (ng) {
    var mod = angular.module('RF4Mod', ['ui.router']);

    mod.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

            $stateProvider.state('rf4', {
                views: {
                    inputView: {
                        templateUrl: "RF4/rf4.input.html",
                        controller: "RF4Ctrl"
                    }
                }
            }).state('rf4Response', {

                params: {
                    fechaInicio: null,
                    fechaFin: null,
                    tipoContrato: null,
                    numPersonas: null,
                    precioReserva: null,
                    alojamiento: null,
                    fechaCreacionOferta: null,
                    cliente: null
                },

                views: {
                    inputView: {
                        templateUrl: "RF4/rf4.input.html"
                    },
                    outputView: {
                        templateUrl: "RF4/rf4.output.html",
                        controller: "responseRF4Ctrl"
                    }
                }
            });
        }]);

})(window.angular);