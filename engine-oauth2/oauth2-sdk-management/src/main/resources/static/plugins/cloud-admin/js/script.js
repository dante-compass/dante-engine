let App = function () {

	let currentPage = ''; // current page

	/*-----------------------------------------------------------------------------------*/
	/*	Uniform
    /*-----------------------------------------------------------------------------------*/
	let handleUniform = function () {
		$(".uniform").uniform();
	}
	return {
		//Initialise theme pages
		init: function () {
			if (App.isPage("login_bg")) {
				handleUniform();	//Function to handle uniform inputs
			}
		},

		//Set page
		setPage: function (name) {
			currentPage = name;
		},

		isPage: function (name) {
			return currentPage === name;
		},

		// wrapper function to scroll(focus) to an element
		scrollTo: function (el, offset) {
			let pos = (el && el.size() > 0) ? el.offset().top : 0;
			jQuery('html,body').animate({
				scrollTop: pos + (offset ? offset : 0)
			}, 'slow');
		},

		// function to scroll to the top
		scrollTop: function () {
			App.scrollTo();
		},
	};
}();
(function (a, b) {
	a.fn.admin_tree = function (d) {
		let c = {
			"open-icon": "fa fa-folder-open",
			"close-icon": "fa fa-folder",
			selectable: true,
			"selected-icon": "fa fa-check",
			"unselected-icon": "tree-dot"
		};
		c = a.extend({}, c, d);
		this.each(function () {
			let e = a(this);
			e.html('<div class = "tree-folder" style="display:none;">				<div class="tree-folder-header">					<i class="' + c["close-icon"] + '"></i>					<div class="tree-folder-name"></div>				</div>				<div class="tree-folder-content"></div>				<div class="tree-loader" style="display:none"></div>			</div>			<div class="tree-item" style="display:none;">				' + (c["unselected-icon"] == null ? "" : '<i class="' + c["unselected-icon"] + '"></i>') + '				<div class="tree-item-name"></div>			</div>');
			e.addClass(c.selectable === true ? "tree-selectable" : "tree-unselectable");
			e.tree(c)
		});
		return this
	}
})(window.jQuery);


(function () {
	this.Theme = (function () {
		function Theme() {
		}

		Theme.colors = {
			white: "#FFFFFF",
			primary: "#5E87B0",
			red: "#D9534F",
			green: "#A8BC7B",
			blue: "#70AFC4",
			orange: "#F0AD4E",
			yellow: "#FCD76A",
			gray: "#6B787F",
			lightBlue: "#D4E5DE",
			purple: "#A696CE",
			pink: "#DB5E8C",
			dark_orange: "#F38630"
		};
		return Theme;
	})();
})(window.jQuery);