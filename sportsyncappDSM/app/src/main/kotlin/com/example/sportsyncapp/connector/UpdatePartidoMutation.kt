
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



public interface UpdatePartidoMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      UpdatePartidoMutation.Data,
      UpdatePartidoMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val eventoId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val deporteId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val equipoLocalId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val equipoVisitanteId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val fecha: com.google.firebase.dataconnect.LocalDate,
  
    val hora: String,
  
    val estadoId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val partidos_update: PartidosKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "UpdatePartido"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun UpdatePartidoMutation.ref(
  
    id: java.util.UUID,eventoId: java.util.UUID,deporteId: java.util.UUID,equipoLocalId: java.util.UUID,equipoVisitanteId: java.util.UUID,fecha: com.google.firebase.dataconnect.LocalDate,hora: String,estadoId: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    UpdatePartidoMutation.Data,
    UpdatePartidoMutation.Variables
  > =
  ref(
    
      UpdatePartidoMutation.Variables(
        id=id,eventoId=eventoId,deporteId=deporteId,equipoLocalId=equipoLocalId,equipoVisitanteId=equipoVisitanteId,fecha=fecha,hora=hora,estadoId=estadoId,
  
      )
    
  )

public suspend fun UpdatePartidoMutation.execute(

  
    
      id: java.util.UUID,eventoId: java.util.UUID,deporteId: java.util.UUID,equipoLocalId: java.util.UUID,equipoVisitanteId: java.util.UUID,fecha: com.google.firebase.dataconnect.LocalDate,hora: String,estadoId: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    UpdatePartidoMutation.Data,
    UpdatePartidoMutation.Variables
  > =
  ref(
    
      id=id,eventoId=eventoId,deporteId=deporteId,equipoLocalId=equipoLocalId,equipoVisitanteId=equipoVisitanteId,fecha=fecha,hora=hora,estadoId=estadoId,
  
    
  ).execute()


