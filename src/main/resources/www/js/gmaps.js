require.config({
	paths: {
		jquery: 'libs/jquery/jquery',
		underscore: 'libs/underscore/underscore',
		gmaps: 'https://maps.googleapis.com/maps/api/js'
	},
	shim: {
		jquery: {exports: '$'},
		underscore: {exports: '_'},
		gmaps: {exports: ['gmaps']}
	}
});

define([
	'jquery',
	'underscore'
], function(jquery, _) {


$.getJSON( "api/networks/2/routers", function( data ) {
	var items = [];
	$.each( data, function( key, val ) {
	   items.push( key + val );
	});
	 
	console.log(items[0]);
	var firstRtr = JSON.stringify(items[0]);
	console.log(firstRtr);
});



var markers = [
  	['Nanterre', 48.892423, 2.215331, 'www.google.fr'],
  	['Crosnes', 48.720171, 2.461891, 'www.yahoo.com'],
  	['Lyon', 45.764043, 4.835659, 'www.cisco.fr'],
  	['Marseille', 43.296482, 5.369780, 'www.twitter.com'],
  	['Toulouse', 43.604652, 1.444209, 'www.sixxs.net'],
  	['Nice', 43.710173, 7.261953, 'www.free.fr'],
  	['Brest', 48.390394, -4.486076, 'www.clamart.fr'],
  	['Poitiers', 46.580224, 0.340375, 'www.renault.fr'],
  	['Bordeaux', 44.837789, -0.579180, 'www.bordeaux.fr']
    ];
  
var links = [
  	['Paris - Bordeaux', [
		{lat: markers[0][1], lng: markers[0][2]},
		{lat: markers[8][1], lng: markers[8][2]}
	]],
	['Poitiers - Nice', [
		{lat: markers[7][1], lng: markers[7][2]},
		{lat: markers[5][1], lng: markers[5][2]}
	]],
	['Nanterre - Brest', [
  		{lat: markers[0][1], lng: markers[0][2]},
  		{lat: markers[6][1], lng: markers[6][2]}
  	]]
];
var arrowForward = {
	path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW
};
var arrowBackward = {
	path: google.maps.SymbolPath.BACKWARD_CLOSED_ARROW
};
var circle = {
	path: google.maps.SymbolPath.CIRCLE,
	scale: 8
};
var icon = {
  	url: 'img/router_red.svg',
	scaledSize: new google.maps.Size(50, 50), // scaled size
	origin: new google.maps.Point(0,0), // origin
	anchor: new google.maps.Point(30,30) // anchor
};

function initialize() {
    var mapCanvas = document.getElementById('map');
	var mapOptions = {mapTypeId: google.maps.MapTypeId.TERRAIN};
	var map = new google.maps.Map(document.getElementById('map'), mapOptions);

	var infowindow = new google.maps.InfoWindow();
	var marker, content, i;
	var bounds = new google.maps.LatLngBounds();

	for (i = 0; i < markers.length; i++) {  
  		var pos = new google.maps.LatLng(markers[i][1], markers[i][2]);
  		bounds.extend(pos);
  		marker = new google.maps.Marker({
    		position: pos,
    		map: map,
    		animation: google.maps.Animation.DROP,
    		icon: icon, 
    		url: markers[i][3]
  		});
  		google.maps.event.addListener(marker, 'mouseover', (function(marker, i) {
			return function() {
    			infowindow.setContent(markers[i][0]);
    			infowindow.open(map, marker);
			}
  		})(marker, i));
  		google.maps.event.addListener(marker, 'mouseout', (function(marker, i) {
  			return function() {
    			infowindow.close(true);
			}
  		})(marker, i));	
  		google.maps.event.addListener(marker, 'click', function() {
  			window.open('http://'+this.url);
  		});	
  		map.fitBounds(bounds);
	}
	for (i = 0; i < links.length; i++) { 
		var line = new google.maps.Polyline({
			path: links[i][1],
			icons: [{
				icon: arrowForward,
				offset: '100%'
			},{
				icon: arrowBackward,
				offset: '0%'
			}],
			strokeColor: "#38692C",
			strokeOpacity: 1,
			strokeWeight: 2,
			map: map
		});
		var shadowLine = new google.maps.Polyline({
			path: links[i][1],
			strokeColor: "#38692C",
			strokeOpacity: 0.07,
			strokeWeight: 20,
			map: map
		});
		mapsInfoWindow(shadowLine, links[i][0], map);
	} 
  };
  function mapsInfoWindow(line, content, map) {
  	var infowindow = new google.maps.InfoWindow();
  	google.maps.event.addListener(line, 'mouseover', function(evt) {
   	infowindow.setContent(content);
   	infowindow.setPosition(evt.latLng);
   	infowindow.open(map);
	});
	google.maps.event.addListener(line, 'mouseout', function(evt) {
		infowindow.close(true);
	});
};

google.maps.event.addDomListener(window, 'load', initialize);  

});