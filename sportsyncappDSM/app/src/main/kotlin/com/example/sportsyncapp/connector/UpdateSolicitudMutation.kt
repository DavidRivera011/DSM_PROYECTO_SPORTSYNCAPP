
@file:Suppress(
  "KotlinRedundantDiagnosticSuppress",
  "PropertyName",
  "MayBeConstant",
  "RedundantVisibilityModifier",
  "RedundantCompanionReference",
  "RemoveEmptyClassBody",
  "SpellCheckingInspection",
  "unused",
)

package com.example.sportsyncapp.connector



public interface UpdateSolicitudMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      UpdateSolicitudMutation.Data,
      UpdateSolicitudMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val estadoId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val solicitudes_update: SolicitudesKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "UpdateSolicitud"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun UpdateSolicitudMutation.ref(
  
    id: java.util.UUID,estadoId: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    UpdateSolicitudMutation.Data,
    UpdateSolicitudMutation.Variables
  > =
  ref(
    
      UpdateSolicitudMutation.Variables(
        id=id,estadoId=estadoId,
  
      )
    
  )

public suspend fun UpdateSolicitudMutation.execute(

  
    
      id: java.util.UUID,estadoId: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    UpdateSolicitudMutation.Data,
    UpdateSolicitudMutation.Variables
  > =
  ref(
    
      id=id,estadoId=estadoId,
  
    
  ).execute()


