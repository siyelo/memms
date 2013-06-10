$(document).ready(function(){

  // load dashboard stuff here

  // filters show/hide
  $('#js-filters-toggle').click(function(e) {
    e.preventDefault();
    $('.filters-box').slideToggle();
  });
  $('#js-filters-close').click(function(e) {
    e.preventDefault();
    $('.filters-box').slideToggle();
  });


  // show/hide dropdowns
  $('.v-tabs-fold-toggle').click(function(event) {
    var toggle = $(this);
    var li = toggle.parent().parent();
    if(li.hasClass('selected')) {
      li.children('.v-tabs-fold-container').children().hide();
      li.removeClass('selected');
    } else {
      li.children('.v-tabs-fold-container').children().show();
      li.addClass('selected');
    }
    li.children('.v-tabs-fold-container').toggle(500);
  });


  $('.v-tabs-fold-toggle-level-two').click(function(event) {
    var toggle = $(this);
    var li = toggle.parent().parent();
    if(li.hasClass('selected')) {
      li.removeClass('selected');
    } else {
      li.addClass('selected');
    }
    li.children('.nested-tab-level-two').toggle(500);
  });



  //Level 1 tabs navigation

  $('.v-tabs-nested-nav').children('li').children('a').click(function(e) {
    var tabs = $(this).parents('div').children('.v-tabs-nested-nav').children('li').children('a');
    var clicked_tab = $(this);
    var parent_div = $(this).parents('div');
    e.preventDefault();
    tabs.removeClass('active');
    clicked_tab.addClass('active');
    var id = $(this).attr('id');
    parent_div.children('.toggled_tab').hide().removeClass('toggled_tab');
    parent_div.children('div#' + id).addClass('toggled_tab');
    parent_div.children('div#' + id).show();
  });


  //Level 0 tabs navigation

  $('#top_tabs').children('li').children('a').click(function(e) {
    e.preventDefault();
    var clicked_tab = $(this);
    var tabs = $('#top_tabs');
    tabs.find('.active').removeClass('active');
    clicked_tab.addClass('active');

    var id = $(this).attr('id');
    var parent_div = clicked_tab.parents('div');

    $('div#corrective').hide();
    $('div#preventive').hide();
    $('div#equipment').hide();
    $('div#spare_parts').hide();
    $('div#monitoring').hide();

    $('div#' + id).show();
  });

});
