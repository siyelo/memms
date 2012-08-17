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
package org.chai.memms.equipment

import org.chai.memms.AbstractEntityController;

/**
 * @author Eugene Munyaneza
 *
 */
class EquipmentTypeController extends AbstractEntityController{
	def equipmentTypeService
	def languageService
	
	def getEntity(def id) {
		return EquipmentType.get(id);
	}

	def createEntity() {
		return new EquipmentType();
	}

	def getTemplate() {
		return "/entity/equipmentType/createEquipmentType";
	}

	def getLabel() {
		return "equipment.type.label";
	}

	def getEntityClass() {
		return EquipmentType.class;
	}
	def deleteEntity(def entity) {
		if (entity.equipments.size() != 0)
			flash.message = message(code: 'equipment.type.hasequipment', args: [message(code: getLabel(), default: 'entity'), params.id], default: 'Equipment Type {0} still has associated equipment.')
		else
			super.deleteEntity(entity);
	}
	
	def bindParams(def entity) {		
		if(!params.id){
			entity.addedOn= new Date()
			entity.lastModifiedOn= new Date()
		}else{
			entity.lastModifiedOn= new Date()
		}
		entity.properties = params		
	}

	def getModel(def entity) {
		[
			type: entity
		]
	}
	def list = {
		adaptParamsForList()
		List<EquipmentType> types = EquipmentType.list(params);
		render(view:"/entity/list",model:[
			template: "equipmentType/equipmentTypeList",
			entities: types,
			entityCount: EquipmentType.count(),
			code: getLabel(),
			entityClass: getEntityClass()
			])
	}
	
	def getAjaxData = {
		List<EquipmentType> types = equipmentTypeService.searchEquipmentType(params['term'], [:])
		render(contentType:"text/json") {
			elements = array {
				types.each { type ->
					elem (
						key: type.id,
						value: type.getNames(languageService.getCurrentLanguage()) + ' ['+type.code+']'
					)
				}
			}
		}
	}
	
	def search = {
		adaptParamsForList()
		List<EquipmentType> types = equipmentTypeService.searchEquipmentType(params['q'], params)	
		render (view: '/entity/list', model:[
			template:"equipmentType/equipmentTypeList",
			entities: types,
			entityCount: equipmentTypeService.countEquipmentType(params['q']),
			code: getLabel()
		])
		
	}
	
	def importer ={
	
	}
	
	def export = {
	
	}

}