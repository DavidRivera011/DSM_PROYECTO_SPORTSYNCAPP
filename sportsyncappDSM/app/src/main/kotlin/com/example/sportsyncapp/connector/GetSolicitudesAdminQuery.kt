
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


import kotlinx.coroutines.flow.filterNotNull as _flow_filterNotNull
import kotlinx.coroutines.flow.map as _flow_map


public interface GetSolicitudesAdminQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      DefaultConnector,
      GetSolicitudesAdminQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val usuarios: Usuarios?,
  
    val solicitudess: List<SolicitudessItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class Usuarios(
  
    val rol: Rol,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class Rol(
  
    val nombreRol: String,
  
  ) {
    
    
  }
      
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class SolicitudessItem(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreDeportivo: String?,
  
    val fotoDeportiva: String?,
  
    val fechaSolicitud: com.google.firebase.dataconnect.LocalDate,
  
    val usuario: Usuario,
  
    val deporte: Deporte,
  
    val tipoPerfil: TipoPerfil,
  
    val institucion: Institucion?,
  
    val equipo: Equipo?,
  
    val estado: Estado,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class Usuario(
  
    val id: String,
  
    val nombre: String,
  
    val apellido: String,
  
    val correo: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class Deporte(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreDeporte: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class TipoPerfil(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombrePerfil: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class Institucion(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombre: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class Equipo(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreEquipo: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class Estado(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreEstado: String,
  
  ) {
    
    
  }
      
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "GetSolicitudesAdmin"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun GetSolicitudesAdminQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    GetSolicitudesAdminQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun GetSolicitudesAdminQuery.execute(

  

  ): com.google.firebase.dataconnect.QueryResult<
    GetSolicitudesAdminQuery.Data,
    Unit
  > =
  ref(
    
  ).execute()


  public fun GetSolicitudesAdminQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<GetSolicitudesAdminQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

