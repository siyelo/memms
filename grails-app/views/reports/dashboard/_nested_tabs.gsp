<g:if test="${filterTemplate!=null}">
<g:render template="/entity/${filterTemplate}" />
</g:if>
<ul class="v-tabs-nested">
  <div class="v-tabs-fold-container-level-two">
    <li class="v-tabs-row">
      <p>
        <a class="v-tabs-name v-tabs-fold-toggle-level-two">
          <span class="v-tabs-switch"><img src="${resource(dir:'images',file:'arrow.png')}"/></span>
          Lorem ipsum dolor sit amet 1
        </a>
      </p>
      <div class='nested-tab-level-two'>
        <g:render template="/reports/dashboard/nested_tabs_level_two" />
      </div>
    </li>
    <li class="v-tabs-row">
      <p>
        <a class="v-tabs-name v-tabs-fold-toggle-level-two">
          <span class="v-tabs-switch"><img src="${resource(dir:'images',file:'arrow.png')}"/></span>
          Lorem ipsum dolor sit amet 1
        </a>
      </p>
      <div class='nested-tab-level-two'>
        <g:render template="/reports/dashboard/nested_tabs_level_two" />
      </div>
    </li>
    <li class="v-tabs-row">
      <p>
        <a class="v-tabs-name v-tabs-fold-toggle-level-two">
          <span class="v-tabs-switch"><img src="${resource(dir:'images',file:'arrow.png')}"/></span>
          Lorem ipsum dolor sit amet 1
        </a>
      </p>
      <div class='nested-tab-level-two'>
        <g:render template="/reports/dashboard/nested_tabs_level_two" />
      </div>
    </li>
  </div>
</ul>
