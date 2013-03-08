/** 
 * Copyright (c) 2012, Clinton Health Access Initiative.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.chai.memms.preventive.maintenance

import org.chai.location.CalculationLocation;
import org.chai.memms.AbstractEntityController;
import org.chai.memms.inventory.Equipment;
import org.chai.memms.security.User.UserType;
import org.chai.memms.preventive.maintenance.PreventiveOrder
import org.chai.memms.preventive.maintenance.PreventiveProcess
import org.chai.memms.preventive.maintenance.PreventiveOrder.PreventiveOrderStatus



/**
 * @author Jean Kahigiso M.
 *
 */
class PreventionController extends AbstractEntityController {
	def preventionService
	def maintenanceProcessService

	def getEntity(def id) {
		return Prevention.get(id);
	}

	def createEntity() {
		return new Prevention();
	}

	def getTemplate() {
		return "/entity/prevention/createPrevention";
	}

	def getLabel() {
		return "prevention.label";
	}

	def getEntityClass() {
		return Prevention.class;
	}

	def getModel(def entity) {
		
		[
			prevention:entity
		]
	}

	def bindParams(def entity) {
		if(!entity.id) entity.addedBy = user
		entity.properties = params
	}

	def list = {
		def order =  PreventiveOrder.get(params.int("order.id"))
		def preventions =  preventionService.getPreventionByOrder(order,params)

		 if(request.xhr){
			 this.ajaxModel(preventions,order,"")
		 }else{
			render (view: '/entity/list', model:model(preventions, order) << [
				template:"prevention/preventionList",
				filterTemplate:"prevention/preventionFilter",
				listTop:"prevention/listTop",
			])
		}

	}

	def search = {
		def order =  PreventiveOrder.get(params.int("order.id"))
		def preventions =  preventionService.searchPrevention(params['q'],order,params)

		 if(request.xhr){
			 this.ajaxModel(preventions,order,params['q'])
		 }else{
			render (view: '/entity/list', model:model(preventions, order) << [
				template:"prevention/preventionList",
				filterTemplate:"prevention/preventionFilter",
				listTop:"prevention/listTop",
			])
		}

	}

	def model(def entities, def order) {
		return [
			entities: entities,
			entityCount: entities.totalCount,
			order:order,
			code:getLabel(),
		]
	}

	def ajaxModel(def entities,def order, def searchTerm) {
		def model = model(entities, order) << [q:searchTerm]
		def listHtml = g.render(template:"/entity/prevention/preventionList",model:model)
		render(contentType:"text/json") { results = [listHtml] }
	}

	def addProcess = {
		Prevention prevention = Prevention.get(params.int("prevention.id"))
		def value = params["value"]
		def result = false
		def html =""
		if (prevention == null || value.equals("") || prevention.order.status.equals(PreventiveOrderStatus.CLOSED))
			response.sendError(404)
		else {
				if (log.isDebugEnabled()) log.debug("addPreventionProcess params: "+params)
				maintenanceProcessService.addPreventionProcess(prevention,value,user)	
				if(prevention!=null){
					result=true
					html = g.render(template:"/templates/processList",model:[processes:prevention.processes,type:'prevention'])
				}
				render(contentType:"text/json") { results = [result,html,'prevention'] }
		}
	}

	def removeProcess = {
		PreventiveProcess  process = PreventiveProcess.get(params.int("process.id"))
		def result = false
		def html =""
		if(!process || process.prevention.order.status.equals(PreventiveOrderStatus.CLOSED)) 
			response.sendError(404)
		else{
			Prevention prevention = maintenanceProcessService.deletePreventionProcess(process,user)
			result = true
			html = g.render(template:"/templates/processList",model:[processes:prevention.processes,type:'prevention'])
		}
		render(contentType:"text/json") { results = [result,html,'prevention']}
	}
	
	
}
