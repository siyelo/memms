package org.chai.memms.maintenance

import java.util.Date;
import java.util.Map;

import org.chai.location.DataLocation;
import org.chai.memms.Initializer;
import org.chai.memms.IntegrationTests
import org.chai.memms.equipment.Equipment;
import org.chai.memms.maintenance.WorkOrder.Criticality;
import org.chai.memms.maintenance.WorkOrder.FailureReason;
import org.chai.memms.maintenance.WorkOrder.OrderStatus;
import org.chai.memms.security.User;
import org.chai.memms.security.User.UserType;

/**
 * @author Eugene
 *
 */
class WorkOrderServiceSpec  extends IntegrationTests{
	
	def workOrderService
	def notificationService
	
	def "can search a work order"(){
		setup:
		setupLocationTree()
		setupEquipment()
		def senderOne = newUser("senderOne", true,true)
		Initializer.newWorkOrder(Equipment.findBySerialNumber(CODE(123)), "Nothing yet", Criticality.NORMAL, OrderStatus.OPEN, User.findByUsername("senderOne"), new Date(),FailureReason.NOTSPECIFIED)

		
		when:
		
		//Search by description
		def workOrdersPassesDescription = workOrderService.searchWorkOrder("Nothing yet",null,null,adaptParamsForList())
		def workOrdersFailsDescription = workOrderService.searchWorkOrder("fails",null,null,adaptParamsForList())
		
		//search by DataLocation
		def workOrdersPassesDataLocation = workOrderService.searchWorkOrder("Nothing",DataLocation.findByCode(KIVUYE),null,[:])
		def workOrdersFailsDataLocation = workOrderService.searchWorkOrder("Nothing",DataLocation.findByCode(BUTARO),null,[:])
		
		//Search by Equipment
		def workOrdersPassesEquipment = workOrderService.searchWorkOrder("Nothing",null,Equipment.findBySerialNumber(CODE(123)),[:])
		
		//Search by equipment serial number
		def workOrdersPassesEquipmentSerialnumber = workOrderService.searchWorkOrder(CODE(123),null,null,adaptParamsForList())
		
		//Search by equipment type
		def workOrdersPassesEquipmentType = workOrderService.searchWorkOrder("acce",null,null,adaptParamsForList())
		then:
		workOrdersFailsDescription.size() == 0
		workOrdersPassesDescription.size() == 1
		
		workOrdersFailsDataLocation.size() == 0
		workOrdersPassesDataLocation.size() == 1
		
		workOrdersPassesEquipment.size() == 1
		
		workOrdersPassesEquipmentSerialnumber.size() == 1
		
		workOrdersPassesEquipmentType.size() == 1
	}
	
	def "can filter workOrders"(){
		setup:
		setupLocationTree()
		setupEquipment()
		def senderOne = newUser("senderOne", true,true)
		new WorkOrder(equipment:Equipment.findBySerialNumber(CODE(123)),description: "Nothing yet",criticality:Criticality.NORMAL,status:OrderStatus.OPEN,
			addedBy:User.findByUsername("senderOne"),openOn: Initializer.getDate(12, 9,2012),assistaceRequested:true,failureReason:FailureReason.NOTSPECIFIED).save(failOnError:true)
			
			new WorkOrder(equipment:Equipment.findBySerialNumber(CODE(123)),description: "Nothing yet",criticality:Criticality.LOW,status:OrderStatus.CLOSEDFIXED,
				addedBy:User.findByUsername("senderOne"),openOn: Initializer.getDate(12, 8,2012), closedOn:Initializer.getDate(12, 11,2012),assistaceRequested:false,failureReason:FailureReason.NOTSPECIFIED).save(failOnError:true)
		
		when:
		
		//Filter by DataLocation
		def workOrdersPassesDataLocation = workOrderService.filterWorkOrders(DataLocation.findByCode(KIVUYE),null,null,null,null,null,null,adaptParamsForList())
		def workOrdersFailsDataLocation = workOrderService.filterWorkOrders(DataLocation.findByCode(BUTARO),null,null,null,null,null,null,adaptParamsForList())
		
		//Filter by Equipment
		def workOrdersEquipment = workOrderService.filterWorkOrders(null,Equipment.findBySerialNumber(CODE(123)),null,null,null,null,null,adaptParamsForList())
		
		//Filter by openOn
		def workOrdersopenOn = workOrderService.filterWorkOrders(null,null,Initializer.getDate(12, 9,2012),null,null,null,null,adaptParamsForList())
		
		//Filter by closedOn
		def workOrdersclosedOn = workOrderService.filterWorkOrders(null,null,null,Initializer.getDate(12, 11,2012),null,null,null,adaptParamsForList())
		
		//Filter by assistaceRequested
		def workOrdersassistaceRequested = workOrderService.filterWorkOrders(null,null,null,null,true,null,null,adaptParamsForList())
		
		
		//Filter by criticality
		def workOrdersCriticality = workOrderService.filterWorkOrders(null,null,null,null,null,Criticality.LOW,null,adaptParamsForList())
		
		//Filter by status
		def workOrdersStatus = workOrderService.filterWorkOrders(null,null,null,null,null,null,OrderStatus.OPEN,adaptParamsForList())
		
		then:
		workOrdersPassesDataLocation.size() == 2
		workOrdersFailsDataLocation.size() == 0
		
		workOrdersEquipment.size() == 2
		
		workOrdersopenOn.size() == 1
		
		workOrdersclosedOn.size() == 1
		
		workOrdersassistaceRequested.size() == 1
		
		workOrdersCriticality.size() == 1
		
		workOrdersStatus.size() == 1
	}
	
	def "can escalate and reescalate a workOrders"(){
		setup:
		setupLocationTree()
		setupEquipment()
		
		def clerk = newUser("clerk", true,true)
		clerk.userType = UserType.DATACLERK
		clerk.location = DataLocation.findByCode(KIVUYE)
		clerk.save(failOnError:true)

		def technicianFacilityOne = newUser("technicianFacilityOne", true,true)
		technicianFacilityOne.userType = UserType.TECHNICIANFACILITY
		technicianFacilityOne.location = DataLocation.findByCode(KIVUYE)
		technicianFacilityOne.save(failOnError:true)

		def technicianFacilityTwo = newUser("technicianFacilityTwo", true,true)
		technicianFacilityTwo.userType = UserType.TECHNICIANFACILITY
		technicianFacilityTwo.location = DataLocation.findByCode(KIVUYE)
		technicianFacilityTwo.save(failOnError:true)
		
		def technicianMoHOne = newUser("technicianMoHOne", true,true)
		technicianMoHOne.userType = UserType.TECHNICIANMOH
		technicianMoHOne.location = DataLocation.findByCode(KIVUYE)
		technicianMoHOne.save(failOnError:true)
		
		def technicianMoHTwo = newUser("technicianMoHTwo", true,true)
		technicianMoHTwo.userType = UserType.TECHNICIANMOH
		technicianMoHTwo.location = DataLocation.findByCode(KIVUYE)
		technicianMoHTwo.save(failOnError:true)

		def workOrder = Initializer.newWorkOrder(Equipment.findBySerialNumber(CODE(123)), "Nothing yet", Criticality.NORMAL, OrderStatus.OPEN, clerk, new Date(),FailureReason.NOTSPECIFIED)
		notificationService.newNotification(workOrder, "Send for rapair",clerk)

		when://Can escalate
		workOrderService.escalateWorkOrder(workOrder,"please follow this up",technicianFacilityOne)
		then:
		workOrder.assistaceRequested == true
		Notification.count() == 6
		workOrder.notificationGroup.size() == 5

		when://Can reescalate
		workOrderService.escalateWorkOrder(workOrder,"follow this up again",clerk)
		then:
		workOrder.assistaceRequested == true
		Notification.count() == 10
		workOrder.notificationGroup.size() == 5
	}
}
